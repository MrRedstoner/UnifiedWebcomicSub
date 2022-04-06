package sk.uniba.grman19.dao.impl;

import static sk.uniba.grman19.util.query.TriFunction.asTri;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import sk.uniba.grman19.dao.SubGroupDAO;
import sk.uniba.grman19.filter.FilterColumn;
import sk.uniba.grman19.filter.FilterMapper;
import sk.uniba.grman19.models.entity.SubGroup;
import sk.uniba.grman19.models.entity.SubGroup_;
import sk.uniba.grman19.util.query.FilterMapperQuery;
import sk.uniba.grman19.util.query.SimpleQuery;

@Component
@Transactional(readOnly = true)
public class SubGroupDAOImpl implements SubGroupDAO {

	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	@Qualifier("groupQueryByName")
	private SimpleQuery<SubGroup, String> queryByName;
	@Autowired
	@Qualifier("groupQueryNonUserById")
	private SimpleQuery<SubGroup, Long> queryNonUserById;
	@Autowired
	@Qualifier("groupQueryByFilter")
	private FilterMapperQuery<SubGroup> queryByFilter;

	@Override
	public Optional<SubGroup> getGroup(Long id) {
		return Optional.ofNullable(entityManager.find(SubGroup.class, id));
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
		SubGroup group = new SubGroup(null, null, true);
		entityManager.persist(group);
		return group;
	}

	@Override
	@Transactional(readOnly = false)
	public SubGroup createPublicGroup(String name, String description) {
		SubGroup group = new SubGroup(name, description, false);
		entityManager.persist(group);
		return group;
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
		return entityManager.merge(group);
	}

	private static FilterMapper makeFilterMapper(CriteriaBuilder cb, Root<SubGroup> root) {
		return new FilterMapper(cb)
			.addNumberFilter(FilterColumn.ID, root.get(SubGroup_.id))
			.addStringFilter(FilterColumn.NAME, root.get(SubGroup_.name))
			.addStringFilter(FilterColumn.DESCRIPTION, root.get(SubGroup_.description))
			.addBooleanFilter(FilterColumn.USER_OWNED, root.get(SubGroup_.userOwned));
	}

	private static Predicate nameEqual(CriteriaBuilder cb, Root<SubGroup> root, String name) {
		return cb.equal(root.get(SubGroup_.name), cb.literal(name));
	}

	private static Predicate idEqual(CriteriaBuilder cb, Root<SubGroup> root, Long id) {
		return cb.equal(root.get(SubGroup_.id), cb.literal(id));
	}

	private static Predicate nonUser(CriteriaBuilder cb, Root<SubGroup> root) {
		return cb.isFalse(root.get(SubGroup_.userOwned));
	}

	@Configuration
	static class Config {
		@Bean(name = "groupQueryByName")
		@PersistenceContext
		SimpleQuery<SubGroup, String> queryByName(EntityManager entityManager) {
			return new SimpleQuery<>(entityManager, SubGroup.class, SubGroupDAOImpl::nameEqual);
		}

		@Bean(name = "groupQueryNonUserById")
		@PersistenceContext
		SimpleQuery<SubGroup, Long> queryNonUserById(EntityManager entityManager) {
			return new SimpleQuery<>(entityManager, SubGroup.class, SubGroupDAOImpl::idEqual, asTri(SubGroupDAOImpl::nonUser));
		}

		@Bean(name = "groupQueryByFilter")
		@PersistenceContext
		FilterMapperQuery<SubGroup> queryByFilter(EntityManager entityManager) {
			return new FilterMapperQuery<>(entityManager, SubGroup.class, SubGroupDAOImpl::makeFilterMapper);
		}
	}
}
