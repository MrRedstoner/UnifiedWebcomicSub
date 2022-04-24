package sk.uniba.grman19.dao.impl;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import sk.uniba.grman19.dao.UWSUserDAO;
import sk.uniba.grman19.models.entity.GroupChildStar;
import sk.uniba.grman19.models.entity.GroupChildStar_;
import sk.uniba.grman19.models.entity.PostSubscription;
import sk.uniba.grman19.models.entity.PostSubscription_;
import sk.uniba.grman19.models.entity.Source;
import sk.uniba.grman19.models.entity.SourceSubscription;
import sk.uniba.grman19.models.entity.SourceSubscription_;
import sk.uniba.grman19.models.entity.SubGroup;
import sk.uniba.grman19.models.entity.SubGroup_;
import sk.uniba.grman19.models.entity.UWSUser;
import sk.uniba.grman19.models.entity.UWSUser_;
import sk.uniba.grman19.util.query.SimpleQuery;

@Component
@Transactional(readOnly = true)
public class UWSUserDAOImpl implements UWSUserDAO {

	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	@Qualifier("userQueryByName")
	private SimpleQuery<UWSUser, String> queryByName;

	@Override
	public Optional<UWSUser> getUser(Long id) {
		return Optional.ofNullable(entityManager.find(UWSUser.class, id));
	}

	@Override
	public Optional<UWSUser> getUser(String name) {
		return queryByName.querySingle(name);
	}

	@Override
	@Transactional(readOnly = false)
	public UWSUser createUWSUser(UWSUser user) {
		if (getUser(user.getName()).isPresent()) {
			throw new IllegalArgumentException("Name already used");
		}
		entityManager.persist(user);
		return user;
	}

	@Override
	@Transactional(readOnly = false)
	public UWSUser saveUWSUser(UWSUser user) {
		return entityManager.merge(user);
	}

	@Override
	public List<UWSUser> resolvePosters(SubGroup subscribe, SubGroup ignore) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<UWSUser> cq = cb.createQuery(UWSUser.class).distinct(true);
		Root<GroupChildStar> root = cq.from(GroupChildStar.class);
		Join<GroupChildStar, SubGroup> children = root.join(GroupChildStar_.child);
		ListJoin<SubGroup, PostSubscription> postSubs = children.join(SubGroup_.postSubs);
		Join<PostSubscription, UWSUser> posters = postSubs.join(PostSubscription_.user);

		Subquery<Source> ignored = cq.subquery(Source.class);
		Root<SourceSubscription> rootIgnored = ignored.from(SourceSubscription.class);
		ignored.where(cb.equal(rootIgnored.get(SourceSubscription_.group), cb.literal(ignore.getId())));
		ignored.select(rootIgnored.get(SourceSubscription_.source));

		cq.select(posters).distinct(true);
		cq.where(cb.equal(root.get(GroupChildStar_.parent), cb.literal(subscribe.getId())), posters.in(ignored).not());
		return entityManager.createQuery(cq).getResultList();
	}

	private static Predicate nameEqual(CriteriaBuilder cb, Root<UWSUser> root, String name) {
		return cb.equal(root.get(UWSUser_.name), cb.literal(name));
	}

	@Configuration
	static class Config {
		@Bean(name = "userQueryByName")
		@PersistenceContext
		SimpleQuery<UWSUser, String> queryByName(EntityManager entityManager) {
			return new SimpleQuery<>(entityManager, UWSUser.class, UWSUserDAOImpl::nameEqual);
		}
	}
}
