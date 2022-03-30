package sk.uniba.grman19.dao.impl;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import sk.uniba.grman19.dao.SubscriptionDAO;
import sk.uniba.grman19.models.entity.GroupChild;
import sk.uniba.grman19.models.entity.GroupChild_;
import sk.uniba.grman19.models.entity.SubGroup;
import sk.uniba.grman19.repository.GroupChildRepository;

@Component
@Transactional(readOnly = true)
public class SubscriptionDAOImpl implements SubscriptionDAO {
	@Autowired
	private EntityManager entityManager;
	@Autowired
	GroupChildRepository groupChildRepo;

	@Transactional(readOnly = false)
	@Override
	public GroupChild addGroupRelation(SubGroup parent, SubGroup child) {
		Optional<GroupChild> groupChild = getGroupRelation(parent, child);
		if (groupChild.isPresent()) {
			return groupChild.get();
		}
		return groupChildRepo.save(new GroupChild(parent, child));
	}

	@Override
	public Optional<GroupChild> getGroupRelation(SubGroup parent, SubGroup child) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<GroupChild> cq = cb.createQuery(GroupChild.class);
		Root<GroupChild> root = cq.from(GroupChild.class);
		cq.select(root);
		cq.where(cb.equal(root.get(GroupChild_.parent), cb.literal(parent.getId())), cb.equal(root.get(GroupChild_.child), cb.literal(child.getId())));
		List<GroupChild> s = entityManager.createQuery(cq).getResultList();
		if (s.isEmpty()) {
			return Optional.empty();
		} else {
			return Optional.of(s.get(0));
		}
	}

	@Transactional(readOnly = false)
	@Override
	public void removeGroupRelation(SubGroup parent, SubGroup child) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaDelete<GroupChild> cq = cb.createCriteriaDelete(GroupChild.class);
		Root<GroupChild> root = cq.from(GroupChild.class);
		cq.where(cb.equal(root.get(GroupChild_.parent), cb.literal(parent.getId())), cb.equal(root.get(GroupChild_.child), cb.literal(child.getId())));
		entityManager.createQuery(cq).executeUpdate();
	}
}
