package sk.uniba.grman19.dao.impl;

import static sk.uniba.grman19.util.FunctionUtils.mapLast;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import sk.uniba.grman19.dao.PollVoteDAO;
import sk.uniba.grman19.models.entity.PollOption;
import sk.uniba.grman19.models.entity.PollOption_;
import sk.uniba.grman19.models.entity.PollVote;
import sk.uniba.grman19.models.entity.PollVote_;
import sk.uniba.grman19.models.entity.Post;
import sk.uniba.grman19.models.entity.UWSUser;
import sk.uniba.grman19.util.query.SimpleQuery;

@Component
@Transactional(readOnly = true)
public class PollVoteDAOImpl implements PollVoteDAO {

	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	@Qualifier("pollVoteByRelatedIds")
	private SimpleQuery<PollVote, Pair<Long, Long>> queryByRelatedIds;

	@Override
	public Optional<PollVote> getUserVote(UWSUser user, Post post) {
		return queryByRelatedIds.querySingle(Pair.with(user.getId(), post.getId()));
	}

	@Override
	@Transactional(readOnly = false)
	public PollVote setUserVote(UWSUser user, PollOption option) {
		Optional<PollVote> vote = getUserVote(user, option.getPost());
		if (vote.isPresent()) {
			PollVote pollVote = vote.get();
			pollVote.setOption(option);
			return entityManager.merge(pollVote);
		} else {
			PollVote pollVote = new PollVote(user, option);
			entityManager.persist(pollVote);
			return pollVote;
		}
	}

	@Override
	public Map<Long, Long> getVoteCounts(Set<Long> options) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<OptionVoteCount> cq = cb.createQuery(OptionVoteCount.class);
		Root<PollVote> root = cq.from(PollVote.class);
		cq.multiselect(root.get(PollVote_.option).get(PollOption_.id), cb.count(root.get(PollVote_.id)));
		cq.where(root.get(PollVote_.option).in(options));
		cq.groupBy(root.get(PollVote_.option));
		List<OptionVoteCount> res = entityManager.createQuery(cq).getResultList();
		return res.stream().collect(Collectors.toMap(ovc -> ovc.opt, ovc -> ovc.count));
	}

	private static class OptionVoteCount {
		private long opt, count;

		@SuppressWarnings("unused")
		public OptionVoteCount(long opt, long count) {
			this.opt = opt;
			this.count = count;
		}

		public String toString() {
			return String.format("%d: %d", opt, count);
		}
	}

	private static Predicate userIdEqual(CriteriaBuilder cb, Root<PollVote> root, Long userId) {
		return cb.equal(root.get(PollVote_.user), cb.literal(userId));
	}

	private static Predicate postIdEqual(CriteriaBuilder cb, Root<PollVote> root, Long postId) {
		Join<PollVote, PollOption> option = root.join(PollVote_.option);
		option.on(cb.equal(root.get(PollVote_.option), option));
		return cb.equal(option.get(PollOption_.post), cb.literal(postId));
	}

	@Configuration
	static class Config {
		@Bean(name = "pollVoteByRelatedIds")
		@PersistenceContext
		SimpleQuery<PollVote, Pair<Long, Long>> queryByRelatedIds(EntityManager entityManager) {
			return new SimpleQuery<>(entityManager, PollVote.class, mapLast(Pair::getValue0, PollVoteDAOImpl::userIdEqual), mapLast(Pair::getValue1, PollVoteDAOImpl::postIdEqual));
		}
	}
}
