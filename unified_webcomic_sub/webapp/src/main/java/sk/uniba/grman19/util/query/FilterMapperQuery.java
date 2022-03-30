package sk.uniba.grman19.util.query;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import sk.uniba.grman19.filter.FilterColumn;
import sk.uniba.grman19.filter.FilterMapper;

public class FilterMapperQuery<E> {
	private final EntityManager entityManager;
	private final Class<E> clazz;
	private final BiFunction<CriteriaBuilder, Root<E>, FilterMapper> with;

	public FilterMapperQuery(EntityManager entityManager, Class<E> clazz, BiFunction<CriteriaBuilder, Root<E>, FilterMapper> with) {
		this.entityManager = entityManager;
		this.clazz = clazz;
		this.with = with;
	}

	public Long queryCount(Map<FilterColumn, String> filters) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<E> root = cq.from(clazz);
		cq.select(cb.count(root));
		cq.where(with.apply(cb, root).processFilters(filters));
		return entityManager.createQuery(cq).getSingleResult();
	}

	public List<E> queryList(int offset, int limit, Map<FilterColumn, String> filters) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<E> cq = cb.createQuery(clazz);
		Root<E> root = cq.from(clazz);
		cq.select(root);
		cq.where(with.apply(cb, root).processFilters(filters));
		return entityManager.createQuery(cq)
			.setFirstResult(offset)
			.setMaxResults(limit)
			.getResultList();
	}
}
