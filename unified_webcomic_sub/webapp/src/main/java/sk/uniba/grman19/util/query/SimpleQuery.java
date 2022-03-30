package sk.uniba.grman19.util.query;

import java.util.Arrays;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class SimpleQuery<E, A> {
	private final EntityManager entityManager;
	private final Class<E> clazz;
	private final TriFunction<CriteriaBuilder, Root<E>, A, Predicate> with;

	public SimpleQuery(EntityManager entityManager, Class<E> clazz, TriFunction<CriteriaBuilder, Root<E>, A, Predicate> with) {
		this.entityManager = entityManager;
		this.clazz = clazz;
		this.with = with;
	}

	@SafeVarargs
	public SimpleQuery(EntityManager entityManager, Class<E> clazz, TriFunction<CriteriaBuilder, Root<E>, A, Predicate>... with) {
		this.entityManager = entityManager;
		this.clazz = clazz;
		this.with = combine(with);
	}

	public Optional<E> querySingle(A argument) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<E> cq = cb.createQuery(clazz);
		Root<E> root = cq.from(clazz);
		cq.select(root);
		cq.where(with.apply(cb, root, argument));

		return entityManager.createQuery(cq)
			.setMaxResults(1)
			.getResultList()
			.stream()
			.findFirst();
	}

	public int executeDelete(A argument) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaDelete<E> cq = cb.createCriteriaDelete(clazz);
		Root<E> root = cq.from(clazz);
		cq.where(with.apply(cb, root, argument));

		return entityManager.createQuery(cq).executeUpdate();
	}

	@SafeVarargs
	private static final <A, E> TriFunction<CriteriaBuilder, Root<E>, A, Predicate> combine(TriFunction<CriteriaBuilder, Root<E>, A, Predicate>... predicates) {
		return (cb, root, arg) -> cb.and(Arrays.stream(predicates)
			.map(p -> p.apply(cb, root, arg))
			.toArray(Predicate[]::new));
	}
}
