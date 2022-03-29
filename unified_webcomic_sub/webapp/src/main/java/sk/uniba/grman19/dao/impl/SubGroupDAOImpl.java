package sk.uniba.grman19.dao.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import sk.uniba.grman19.dao.SubGroupDAO;
import sk.uniba.grman19.filter.FilterColumn;
import sk.uniba.grman19.filter.FilterMapper;
import sk.uniba.grman19.models.entity.SubGroup;
import sk.uniba.grman19.models.entity.SubGroup_;
import sk.uniba.grman19.repository.SubGroupRepository;

@Component
@Transactional(readOnly = true)
public class SubGroupDAOImpl implements SubGroupDAO {
	@Autowired
	private EntityManager entityManager;
	@Autowired
	private SubGroupRepository repository;

	@Override
	public Optional<SubGroup> getGroup(Long id) {
		return repository.findById(id);
	}

	@Override
	@Transactional(readOnly = false)
	public SubGroup createUserGroup() {
		return repository.save(new SubGroup(null, null, true));
	}

	@Override
	@Transactional(readOnly = false)
	public SubGroup createPublicGroup(String name, String description) {
		return repository.save(new SubGroup(name, description, false));
	}

	@Override
	public long getSubGroupCount(Map<FilterColumn, String> filters) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<SubGroup> root = cq.from(SubGroup.class);
		cq.select(cb.count(root));
		cq.where(makeFilterMapper(cb, root).processFilters(filters));
		return entityManager.createQuery(cq).getSingleResult();
	}

	@Override
	public List<SubGroup> getSubGroups(int offset, int limit, Map<FilterColumn, String> filters) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<SubGroup> cq = cb.createQuery(SubGroup.class);
		Root<SubGroup> root = cq.from(SubGroup.class);
		cq.select(root);
		cq.where(makeFilterMapper(cb, root).processFilters(filters));
		return entityManager.createQuery(cq)
			.setFirstResult(offset)
			.setMaxResults(limit)
			.getResultList();
	}

	private FilterMapper makeFilterMapper(CriteriaBuilder cb, Root<SubGroup> root) {
		return new FilterMapper(cb)
			.addNumberFilter(FilterColumn.ID, root.get(SubGroup_.id))
			.addStringFilter(FilterColumn.NAME, root.get(SubGroup_.name))
			.addStringFilter(FilterColumn.DESCRIPTION, root.get(SubGroup_.description))
			.addBooleanFilter(FilterColumn.USER_OWNED, root.get(SubGroup_.userOwned));
	}

	@Override
	public SubGroup saveGroup(SubGroup group) {
		return repository.save(group);
	}
}
