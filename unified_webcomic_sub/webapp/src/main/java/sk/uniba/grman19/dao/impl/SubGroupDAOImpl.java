package sk.uniba.grman19.dao.impl;

import static sk.uniba.grman19.util.query.TriFunction.asTri;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
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
import sk.uniba.grman19.util.query.FilterMapperQuery;
import sk.uniba.grman19.util.query.SimpleQuery;

@Component
@Transactional(readOnly = true)
public class SubGroupDAOImpl implements SubGroupDAO {

	@Autowired
	public SubGroupDAOImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
		this.queryByName = new SimpleQuery<>(entityManager, SubGroup.class, this::nameEqual);
		this.queryNonUserById = new SimpleQuery<>(entityManager, SubGroup.class, this::idEqual, asTri(this::nonUser));
		this.queryByFilter = new FilterMapperQuery<>(entityManager, SubGroup.class, this::makeFilterMapper);
	}

	@Autowired
	private SubGroupRepository repository;
	@SuppressWarnings("unused")
	private final EntityManager entityManager;
	private final SimpleQuery<SubGroup, String> queryByName;
	private final SimpleQuery<SubGroup, Long> queryNonUserById;
	private final FilterMapperQuery<SubGroup> queryByFilter;

	@Override
	public Optional<SubGroup> getGroup(Long id) {
		return repository.findById(id);
	}

	@Override
	public Optional<SubGroup> getNonUserGroup(Long id) {
		return queryNonUserById.querySingle(id);
	}

	@Override
	public Optional<SubGroup> getGroup(String name) {
		return queryByName.querySingle(name);
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
		return queryByFilter.queryCount(filters);
	}

	@Override
	public List<SubGroup> getSubGroups(int offset, int limit, Map<FilterColumn, String> filters) {
		return queryByFilter.queryList(offset, limit, filters);
	}

	@Override
	@Transactional(readOnly = false)
	public SubGroup saveGroup(SubGroup group) {
		return repository.save(group);
	}

	private FilterMapper makeFilterMapper(CriteriaBuilder cb, Root<SubGroup> root) {
		return new FilterMapper(cb)
			.addNumberFilter(FilterColumn.ID, root.get(SubGroup_.id))
			.addStringFilter(FilterColumn.NAME, root.get(SubGroup_.name))
			.addStringFilter(FilterColumn.DESCRIPTION, root.get(SubGroup_.description))
			.addBooleanFilter(FilterColumn.USER_OWNED, root.get(SubGroup_.userOwned));
	}

	private Predicate nameEqual(CriteriaBuilder cb, Root<SubGroup> root, String name) {
		return cb.equal(root.get(SubGroup_.name), cb.literal(name));
	}

	private Predicate idEqual(CriteriaBuilder cb, Root<SubGroup> root, Long id) {
		return cb.equal(root.get(SubGroup_.id), cb.literal(id));
	}

	private Predicate nonUser(CriteriaBuilder cb, Root<SubGroup> root) {
		return cb.isFalse(root.get(SubGroup_.userOwned));
	}
}
