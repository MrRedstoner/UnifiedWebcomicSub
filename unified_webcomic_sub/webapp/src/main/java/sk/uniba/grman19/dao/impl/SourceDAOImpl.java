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

import sk.uniba.grman19.dao.SourceDAO;
import sk.uniba.grman19.filter.FilterColumn;
import sk.uniba.grman19.filter.FilterMapper;
import sk.uniba.grman19.models.entity.GroupChildStar;
import sk.uniba.grman19.models.entity.GroupChildStar_;
import sk.uniba.grman19.models.entity.Source;
import sk.uniba.grman19.models.entity.SourceAttribute;
import sk.uniba.grman19.models.entity.SourceAttribute_;
import sk.uniba.grman19.models.entity.SourceSubscription;
import sk.uniba.grman19.models.entity.SourceSubscription_;
import sk.uniba.grman19.models.entity.Source_;
import sk.uniba.grman19.models.entity.SubGroup;
import sk.uniba.grman19.models.entity.SubGroup_;
import sk.uniba.grman19.util.query.FilterMapperQuery;
import sk.uniba.grman19.util.query.SimpleQuery;

@Component
@Transactional(readOnly = true)
public class SourceDAOImpl implements SourceDAO {

	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	@Qualifier("sourceQueryByName")
	private SimpleQuery<Source, String> queryByName;
	@Autowired
	@Qualifier("sourceQueryByFilter")
	private FilterMapperQuery<Source> queryByFilter;

	@Override
	public Optional<Source> getSource(Long id) {
		return Optional.ofNullable(entityManager.find(Source.class, id));
	}

	@Override
	public long getSourceCount(Map<FilterColumn, String> filters) {
		return queryByFilter.queryCount(filters);
	}

	@Override
	public Optional<Source> getSource(String name) {
		return queryByName.querySingle(name);
	}

	@Override
	@Transactional(readOnly = false)
	public Source createSource(String name, String description) {
		Source source = new Source(name, description);
		entityManager.persist(source);
		return source;
	}

	@Override
	public List<Source> getSources(int offset, int limit, Map<FilterColumn, String> filters) {
		return queryByFilter.queryList(offset, limit, filters);
	}

	@Override
	@Transactional(readOnly = false)
	public Source saveSource(Source source) {
		return entityManager.merge(source);
	}

	@Override
	public List<Source> getSourcesByAttribute(String key, String value) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Source> cq = cb.createQuery(Source.class).distinct(true);
		Root<Source> root = cq.from(Source.class);

		// fetch attributes
		root.fetch(Source_.sourceAttribute);
		// subquery for a matchin atribute
		Subquery<SourceAttribute> attribute = cq.subquery(SourceAttribute.class);
		Root<SourceAttribute> attributeRoot = attribute.from(SourceAttribute.class);
		attribute.select(attributeRoot);
		attribute.where(cb.equal(root.get(Source_.id), attributeRoot.get(SourceAttribute_.source)), cb.equal(attributeRoot.get(SourceAttribute_.name), cb.literal(key)),
				cb.equal(attributeRoot.get(SourceAttribute_.value), cb.literal(value)));

		cq.select(root);
		cq.where(cb.exists(attribute));
		return entityManager.createQuery(cq).getResultList();
	}

	@Override
	public List<Source> resolveSources(SubGroup subscribe, SubGroup ignore) {
		// TODO implement ignore
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Source> cq = cb.createQuery(Source.class).distinct(true);
		Root<GroupChildStar> root = cq.from(GroupChildStar.class);
		Join<GroupChildStar, SubGroup> children = root.join(GroupChildStar_.child);
		ListJoin<SubGroup, SourceSubscription> sourceSubs = children.join(SubGroup_.sourceSubs);
		Join<SourceSubscription, Source> sources = sourceSubs.join(SourceSubscription_.source);

		Subquery<Source> ignored = cq.subquery(Source.class);
		Root<SourceSubscription> rootIgnored = ignored.from(SourceSubscription.class);
		ignored.where(cb.equal(rootIgnored.get(SourceSubscription_.group), cb.literal(ignore.getId())));
		ignored.select(rootIgnored.get(SourceSubscription_.source));

		cq.select(sources).distinct(true);
		cq.where(cb.equal(root.get(GroupChildStar_.parent), cb.literal(subscribe.getId())), sources.in(ignored).not());
		return entityManager.createQuery(cq).getResultList();
	}

	private static FilterMapper makeFilterMapper(CriteriaBuilder cb, Root<Source> root) {
		return new FilterMapper(cb)
			.addNumberFilter(FilterColumn.ID, root.get(Source_.id))
			.addStringFilter(FilterColumn.NAME, root.get(Source_.name))
			.addStringFilter(FilterColumn.DESCRIPTION, root.get(Source_.description));
	}

	private static Predicate nameEqual(CriteriaBuilder cb, Root<Source> root, String name) {
		return cb.equal(root.get(Source_.name), cb.literal(name));
	}

	@Configuration
	static class Config {
		@Bean(name = "sourceQueryByName")
		@PersistenceContext
		SimpleQuery<Source, String> queryByName(EntityManager entityManager) {
			return new SimpleQuery<>(entityManager, Source.class, SourceDAOImpl::nameEqual);
		}

		@Bean(name = "sourceQueryByFilter")
		@PersistenceContext
		FilterMapperQuery<Source> queryByFilter(EntityManager entityManager) {
			return new FilterMapperQuery<>(entityManager, Source.class, SourceDAOImpl::makeFilterMapper);
		}
	}
}
