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

import sk.uniba.grman19.dao.SubscriptionDAO;
import sk.uniba.grman19.models.entity.GroupChild;
import sk.uniba.grman19.models.entity.GroupChild_;
import sk.uniba.grman19.models.entity.PostSubscription;
import sk.uniba.grman19.models.entity.PostSubscription_;
import sk.uniba.grman19.models.entity.Source;
import sk.uniba.grman19.models.entity.SourceSubscription;
import sk.uniba.grman19.models.entity.SourceSubscription_;
import sk.uniba.grman19.models.entity.SubGroup;
import sk.uniba.grman19.models.entity.UWSUser;
import sk.uniba.grman19.util.query.SimpleQuery;

@Component
@Transactional(readOnly = true)
public class SubscriptionDAOImpl implements SubscriptionDAO {

	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	@Qualifier("subscriptionQueryByRelatedIds")
	private SimpleQuery<GroupChild, Pair<Long, Long>> queryByRelatedIds;
	@Autowired
	@Qualifier("subscriptionQuerySourceByIds")
	private SimpleQuery<SourceSubscription, Pair<Long, Long>> querySourceByIds;
	@Autowired
	@Qualifier("subscriptionQueryPosterByIds")
	private SimpleQuery<PostSubscription, Pair<Long, Long>> queryPosterByIds;

	@Override
	@Transactional(readOnly = false)
	public GroupChild addGroupRelation(SubGroup parent, SubGroup child) {
		Optional<GroupChild> groupChild = getGroupRelation(parent, child);
		if (groupChild.isPresent()) {
			return groupChild.get();
		}
		GroupChild newChild = new GroupChild(parent, child);
		entityManager.persist(newChild);
		return newChild;
	}

	@Override
	public Optional<GroupChild> getGroupRelation(SubGroup parent, SubGroup child) {
		return queryByRelatedIds.querySingle(Pair.with(parent.getId(), child.getId()));
	}

	@Override
	@Transactional(readOnly = false)
	public void removeGroupRelation(SubGroup parent, SubGroup child) {
		queryByRelatedIds.executeDelete(Pair.with(parent.getId(), child.getId()));
	}

	@Override
	@Transactional(readOnly = false)
	public SourceSubscription addSourceSubscription(SubGroup group, Source source) {
		Optional<SourceSubscription> sourceSubscription = getSourceSubscription(group, source);
		if (sourceSubscription.isPresent()) {
			return sourceSubscription.get();
		}
		SourceSubscription newSub = new SourceSubscription(group, source);
		entityManager.persist(newSub);
		return newSub;
	}

	@Override
	public Optional<SourceSubscription> getSourceSubscription(SubGroup group, Source source) {
		return querySourceByIds.querySingle(Pair.with(group.getId(), source.getId()));
	}

	@Override
	@Transactional(readOnly = false)
	public void removeSourceSubscription(SubGroup group, Source source) {
		querySourceByIds.executeDelete(Pair.with(group.getId(), source.getId()));
	}

	@Override
	public Optional<PostSubscription> getPostSubscription(SubGroup group, UWSUser poster) {
		return queryPosterByIds.querySingle(Pair.with(group.getId(), poster.getId()));
	}

	@Override
	@Transactional(readOnly = false)
	public PostSubscription addPosterSubscription(SubGroup group, UWSUser poster) {
		Optional<PostSubscription> postSubscription = getPostSubscription(group, poster);
		if (postSubscription.isPresent()) {
			return postSubscription.get();
		}
		PostSubscription newSub = new PostSubscription(group, poster);
		entityManager.persist(newSub);
		return newSub;
	}

	@Override
	@Transactional(readOnly = false)
	public void removePosterSubscription(SubGroup group, UWSUser poster) {
		queryPosterByIds.executeDelete(Pair.with(group.getId(), poster.getId()));
	}

	private static Predicate parentIdEqual(CriteriaBuilder cb, Root<GroupChild> root, Long parentId) {
		return cb.equal(root.get(GroupChild_.parent), cb.literal(parentId));
	}

	private static Predicate childIdEqual(CriteriaBuilder cb, Root<GroupChild> root, Long childId) {
		return cb.equal(root.get(GroupChild_.child), cb.literal(childId));
	}

	private static Predicate groupIdEqualSource(CriteriaBuilder cb, Root<SourceSubscription> root, Long parentId) {
		return cb.equal(root.get(SourceSubscription_.group), cb.literal(parentId));
	}

	private static Predicate groupIdEqualPost(CriteriaBuilder cb, Root<PostSubscription> root, Long parentId) {
		return cb.equal(root.get(PostSubscription_.group), cb.literal(parentId));
	}

	private static Predicate sourceIdEqual(CriteriaBuilder cb, Root<SourceSubscription> root, Long childId) {
		return cb.equal(root.get(SourceSubscription_.source), cb.literal(childId));
	}

	private static Predicate posterIdEqual(CriteriaBuilder cb, Root<PostSubscription> root, Long childId) {
		return cb.equal(root.get(PostSubscription_.user), cb.literal(childId));
	}

	@Configuration
	static class Config {
		@Bean(name = "subscriptionQueryByRelatedIds")
		@PersistenceContext
		SimpleQuery<GroupChild, Pair<Long, Long>> queryByRelatedIds(EntityManager entityManager) {
			return new SimpleQuery<>(entityManager, GroupChild.class, mapLast(Pair::getValue0, SubscriptionDAOImpl::parentIdEqual), mapLast(Pair::getValue1, SubscriptionDAOImpl::childIdEqual));
		}

		@Bean(name = "subscriptionQuerySourceByIds")
		@PersistenceContext
		SimpleQuery<SourceSubscription, Pair<Long, Long>> querySourceByIds(EntityManager entityManager) {
			return new SimpleQuery<>(entityManager, SourceSubscription.class, mapLast(Pair::getValue0, SubscriptionDAOImpl::groupIdEqualSource),
					mapLast(Pair::getValue1, SubscriptionDAOImpl::sourceIdEqual));
		}

		@Bean(name = "subscriptionQueryPosterByIds")
		@PersistenceContext
		SimpleQuery<PostSubscription, Pair<Long, Long>> queryPosterByIds(EntityManager entityManager) {
			return new SimpleQuery<>(entityManager, PostSubscription.class, mapLast(Pair::getValue0, SubscriptionDAOImpl::groupIdEqualPost),
					mapLast(Pair::getValue1, SubscriptionDAOImpl::posterIdEqual));
		}
	}
}
