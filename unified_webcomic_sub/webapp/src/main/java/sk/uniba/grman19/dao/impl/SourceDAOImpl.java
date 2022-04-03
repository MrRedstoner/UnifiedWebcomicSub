package sk.uniba.grman19.dao.impl;

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

import sk.uniba.grman19.dao.SourceDAO;
import sk.uniba.grman19.filter.FilterColumn;
import sk.uniba.grman19.filter.FilterMapper;
import sk.uniba.grman19.models.entity.Source;
import sk.uniba.grman19.models.entity.Source_;
import sk.uniba.grman19.repository.SourceRepository;
import sk.uniba.grman19.util.query.FilterMapperQuery;
import sk.uniba.grman19.util.query.SimpleQuery;

@Component
@Transactional(readOnly = true)
public class SourceDAOImpl implements SourceDAO {
	@Autowired
	public SourceDAOImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
		this.queryByName = new SimpleQuery<>(entityManager, Source.class, this::nameEqual);
		this.queryByFilter = new FilterMapperQuery<>(entityManager, Source.class, this::makeFilterMapper);
	}

	@Autowired
	private SourceRepository sourceRepository;
	private final EntityManager entityManager;
	private final SimpleQuery<Source, String> queryByName;
	private final FilterMapperQuery<Source> queryByFilter;

	@Override
	public Optional<Source> getSource(Long id) {
		return sourceRepository.findById(id);
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
	public Source createSource(String name, String description) {
		return saveSource(new Source(name, description));
	}

	@Override
	public List<Source> getSources(int offset, int limit, Map<FilterColumn, String> filters) {
		return queryByFilter.queryList(offset, limit, filters);
	}

	@Override
	public Source saveSource(Source source) {
		return entityManager.merge(source);
	}

	private FilterMapper makeFilterMapper(CriteriaBuilder cb, Root<Source> root) {
		return new FilterMapper(cb)
			.addNumberFilter(FilterColumn.ID, root.get(Source_.id))
			.addStringFilter(FilterColumn.NAME, root.get(Source_.name))
			.addStringFilter(FilterColumn.DESCRIPTION, root.get(Source_.description));
	}

	private Predicate nameEqual(CriteriaBuilder cb, Root<Source> root, String name) {
		return cb.equal(root.get(Source_.name), cb.literal(name));
	}
}
