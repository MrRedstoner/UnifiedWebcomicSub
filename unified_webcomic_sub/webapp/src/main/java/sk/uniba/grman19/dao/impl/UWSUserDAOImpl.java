package sk.uniba.grman19.dao.impl;

import java.util.List;
import java.util.Map;
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
import sk.uniba.grman19.filter.FilterColumn;
import sk.uniba.grman19.filter.FilterMapper;
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
import sk.uniba.grman19.util.query.FilterMapperQuery;
import sk.uniba.grman19.util.query.SimpleQuery;

@Component
@Transactional(readOnly = true)
public class UWSUserDAOImpl implements UWSUserDAO {

	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	@Qualifier("userQueryByName")
	private SimpleQuery<UWSUser, String> queryByName;
	@Autowired
	@Qualifier("userQueryByFilter")
	private FilterMapperQuery<UWSUser> queryByFilter;

	@Override
	public Optional<UWSUser> getUser(Long id) {
		return Optional.ofNullable(entityManager.find(UWSUser.class, id));
	}

	@Override
	public Optional<UWSUser> getUser(String name) {
		return queryByName.querySingle(name);
	}

	@Override
	public long getUserCount(Map<FilterColumn, String> filters) {
		return queryByFilter.queryCount(filters);
	}

	@Override
	public List<UWSUser> getUsers(Integer offset, Integer limit, Map<FilterColumn, String> filters) {
		return queryByFilter.queryList(offset, limit, filters);
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

	private static FilterMapper makeFilterMapper(CriteriaBuilder cb, Root<UWSUser> root) {
		return new FilterMapper(cb)
			.addNumberFilter(FilterColumn.ID, root.get(UWSUser_.id))
			.addStringFilter(FilterColumn.NAME, root.get(UWSUser_.name))
			.addBooleanFilter(FilterColumn.CAN_CREATE_POST, cb.or(cb.isTrue(root.get(UWSUser_.createPost)), cb.isTrue(root.get(UWSUser_.admin)), cb.isTrue(root.get(UWSUser_.owner))));
	}

	@Configuration
	static class Config {
		@Bean(name = "userQueryByName")
		@PersistenceContext
		SimpleQuery<UWSUser, String> queryByName(EntityManager entityManager) {
			return new SimpleQuery<>(entityManager, UWSUser.class, UWSUserDAOImpl::nameEqual);
		}

		@Bean(name = "userQueryByFilter")
		@PersistenceContext
		FilterMapperQuery<UWSUser> queryByFilter(EntityManager entityManager) {
			return new FilterMapperQuery<>(entityManager, UWSUser.class, UWSUserDAOImpl::makeFilterMapper);
		}
	}
}
