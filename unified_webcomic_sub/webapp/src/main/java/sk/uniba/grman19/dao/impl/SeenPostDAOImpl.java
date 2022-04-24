package sk.uniba.grman19.dao.impl;

import static sk.uniba.grman19.util.FunctionUtils.mapLast;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import sk.uniba.grman19.dao.SeenPostDAO;
import sk.uniba.grman19.models.entity.Post;
import sk.uniba.grman19.models.entity.SeenPost;
import sk.uniba.grman19.models.entity.SeenPost_;
import sk.uniba.grman19.models.entity.UWSUser;
import sk.uniba.grman19.util.query.SimpleQuery;

@Component
@Transactional(readOnly = true)
public class SeenPostDAOImpl implements SeenPostDAO {

	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	@Qualifier("seenPostQueryByUserUpdate")
	private SimpleQuery<SeenPost, Pair<Long, Long>> querySourceByIds;

	@Transactional(readOnly = false)
	public SeenPost createSeenPost(UWSUser user, Post update) {
		SeenPost seen = new SeenPost(user, update);
		entityManager.persist(seen);
		return seen;
	}

	@Override
	@Transactional(readOnly = false)
	public SeenPost updateSeenPost(UWSUser user, Post post) {
		Optional<SeenPost> lastUpdate = querySourceByIds.querySingle(Pair.with(user.getId(), post.getId()));
		if (lastUpdate.isPresent()) {
			SeenPost seenUpdate = lastUpdate.get();
			seenUpdate.setPost(post);
			return entityManager.merge(seenUpdate);
		}
		return createSeenPost(user, post);
	}

	private static Predicate userEqual(CriteriaBuilder cb, Root<SeenPost> root, Long user) {
		return cb.equal(root.get(SeenPost_.user), cb.literal(user));
	}

	private static Predicate postEqual(CriteriaBuilder cb, Root<SeenPost> root, Long update) {
		return cb.equal(root.get(SeenPost_.post), cb.literal(update));
	}

	@Configuration
	static class Config {
		@Bean(name = "seenPostQueryByUserUpdate")
		@PersistenceContext
		SimpleQuery<SeenPost, Pair<Long, Long>> querySourceByIds(EntityManager entityManager) {
			return new SimpleQuery<>(entityManager, SeenPost.class, mapLast(Pair::getValue0, SeenPostDAOImpl::userEqual), mapLast(Pair::getValue1, SeenPostDAOImpl::postEqual));
		}
	}

}
