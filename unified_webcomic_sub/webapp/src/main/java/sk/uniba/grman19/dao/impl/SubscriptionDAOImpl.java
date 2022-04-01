package sk.uniba.grman19.dao.impl;

import static sk.uniba.grman19.util.FunctionUtils.mapLast;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import sk.uniba.grman19.dao.SubscriptionDAO;
import sk.uniba.grman19.models.entity.GroupChild;
import sk.uniba.grman19.models.entity.GroupChild_;
import sk.uniba.grman19.models.entity.Source;
import sk.uniba.grman19.models.entity.SourceSubscription;
import sk.uniba.grman19.models.entity.SourceSubscription_;
import sk.uniba.grman19.models.entity.SubGroup;
import sk.uniba.grman19.repository.GroupChildRepository;
import sk.uniba.grman19.repository.SourceSubscriptionRepository;
import sk.uniba.grman19.util.query.SimpleQuery;

@Component
@Transactional(readOnly = true)
public class SubscriptionDAOImpl implements SubscriptionDAO {

	@Autowired
	public SubscriptionDAOImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
		this.queryByRelatedIds = new SimpleQuery<>(entityManager, GroupChild.class, mapLast(Pair::getValue0, this::parentIdEqual), mapLast(Pair::getValue1, this::childIdEqual));
		this.querySourceByIds = new SimpleQuery<>(entityManager, SourceSubscription.class, mapLast(Pair::getValue0, this::groupIdEqual), mapLast(Pair::getValue1, this::sourceIdEqual));
	}

	@Autowired
	GroupChildRepository groupChildRepo;
	@Autowired
	SourceSubscriptionRepository sourceSubRepo;
	@SuppressWarnings("unused")
	private final EntityManager entityManager;
	private final SimpleQuery<GroupChild, Pair<Long, Long>> queryByRelatedIds;
	private final SimpleQuery<SourceSubscription, Pair<Long, Long>> querySourceByIds;

	@Override
	@Transactional(readOnly = false)
	public GroupChild addGroupRelation(SubGroup parent, SubGroup child) {
		Optional<GroupChild> groupChild = getGroupRelation(parent, child);
		if (groupChild.isPresent()) {
			return groupChild.get();
		}
		return groupChildRepo.save(new GroupChild(parent, child));
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
		return sourceSubRepo.save(new SourceSubscription(group, source));
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

	private Predicate parentIdEqual(CriteriaBuilder cb, Root<GroupChild> root, Long parentId) {
		return cb.equal(root.get(GroupChild_.parent), cb.literal(parentId));
	}

	private Predicate childIdEqual(CriteriaBuilder cb, Root<GroupChild> root, Long childId) {
		return cb.equal(root.get(GroupChild_.child), cb.literal(childId));
	}

	private Predicate groupIdEqual(CriteriaBuilder cb, Root<SourceSubscription> root, Long parentId) {
		return cb.equal(root.get(SourceSubscription_.group), cb.literal(parentId));
	}

	private Predicate sourceIdEqual(CriteriaBuilder cb, Root<SourceSubscription> root, Long childId) {
		return cb.equal(root.get(SourceSubscription_.source), cb.literal(childId));
	}
}
