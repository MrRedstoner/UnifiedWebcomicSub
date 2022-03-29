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

import sk.uniba.grman19.dao.SourceDAO;
import sk.uniba.grman19.filter.FilterColumn;
import sk.uniba.grman19.filter.FilterMapper;
import sk.uniba.grman19.models.entity.Source;
import sk.uniba.grman19.models.entity.Source_;
import sk.uniba.grman19.repository.SourceRepository;

@Component
@Transactional(readOnly = true)
public class SourceDAOImpl implements SourceDAO {
	@Autowired
	private EntityManager entityManager;
	@Autowired
	private SourceRepository sourceRepository;

	@Override
	public Optional<Source> getSource(Long id) {
		return sourceRepository.findById(id);
	}

	@Override
	public long getSourceCount(Map<FilterColumn, String> filters) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Source> root = cq.from(Source.class);
		cq.select(cb.count(root));
		cq.where(makeFilterMapper(cb, root).processFilters(filters));
		return entityManager.createQuery(cq).getSingleResult();
	}

	@Override
	public List<Source> getSources(int offset, int limit, Map<FilterColumn, String> filters) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Source> cq = cb.createQuery(Source.class);
		Root<Source> root = cq.from(Source.class);
		cq.select(root);
		cq.where(makeFilterMapper(cb, root).processFilters(filters));
		return entityManager.createQuery(cq)
			.setFirstResult(offset)
			.setMaxResults(limit)
			.getResultList();
	}

	private FilterMapper makeFilterMapper(CriteriaBuilder cb, Root<Source> root) {
		return new FilterMapper(cb)
			.addNumberFilter(FilterColumn.ID, root.get(Source_.id))
			.addStringFilter(FilterColumn.NAME, root.get(Source_.name))
			.addStringFilter(FilterColumn.DESCRIPTION, root.get(Source_.description));
	}
}
