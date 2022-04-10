package sk.uniba.grman19.dao.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import sk.uniba.grman19.dao.SourceUpdateDAO;
import sk.uniba.grman19.models.entity.SeenUpdate;
import sk.uniba.grman19.models.entity.SeenUpdate_;
import sk.uniba.grman19.models.entity.Source;
import sk.uniba.grman19.models.entity.SourceUpdate;
import sk.uniba.grman19.models.entity.SourceUpdate_;

@Component
@Transactional(readOnly = true)
public class SourceUpdateDAOImpl implements SourceUpdateDAO {
	private static final Date DATE_NEVER = new Date(0l);

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@Transactional(readOnly = false)
	public SourceUpdate saveSourceUpdate(Source source, String value, Date date) {
		SourceUpdate update = new SourceUpdate(source, value, date);
		entityManager.persist(update);
		return update;
	}

	@Override
	public List<SourceUpdate> getLastUpdates(Set<Long> ids) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<SourceUpdate> cq = cb.createQuery(SourceUpdate.class);
		Root<SourceUpdate> root = cq.from(SourceUpdate.class);

		// get max date for a source
		Subquery<Date> maxDate = cq.subquery(Date.class);
		Root<SourceUpdate> maxDateRoot = maxDate.from(SourceUpdate.class);
		maxDate.select(cb.greatest(maxDateRoot.get(SourceUpdate_.updateTime)));
		maxDate.where(cb.equal(maxDateRoot.get(SourceUpdate_.source), root.get(SourceUpdate_.source)));

		cq.select(root);
		cq.where(root.get(SourceUpdate_.source).in(ids), cb.equal(root.get(SourceUpdate_.updateTime), maxDate));
		return entityManager.createQuery(cq).getResultList();
	}

	@Override
	public List<SourceUpdate> getUnseenUpdates(Long userId, Set<Long> sourceIds) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<SourceUpdate> cq = cb.createQuery(SourceUpdate.class);
		Root<SourceUpdate> root = cq.from(SourceUpdate.class);

		// filter out those with seen
		Subquery<Date> seen = cq.subquery(Date.class);
		Root<SeenUpdate> seenRoot = seen.from(SeenUpdate.class);
		Join<SeenUpdate, SourceUpdate> seenUpdate = seenRoot.join(SeenUpdate_.update);
		seen.where(cb.equal(seenRoot.get(SeenUpdate_.user), cb.literal(userId)), cb.equal(root.get(SourceUpdate_.source), seenUpdate.get(SourceUpdate_.source)));
		seen.select(cb.coalesce(cb.greatest(seenUpdate.get(SourceUpdate_.updateTime)), cb.literal(DATE_NEVER)));

		root.fetch(SourceUpdate_.source);
		cq.select(root);
		cq.where(root.get(SourceUpdate_.source).in(sourceIds), cb.greaterThan(root.get(SourceUpdate_.updateTime), seen));
		cq.orderBy(cb.asc(root.get(SourceUpdate_.source)));
		return entityManager.createQuery(cq).getResultList();
	}
}
