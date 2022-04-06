package sk.uniba.grman19.dao.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import sk.uniba.grman19.dao.SourceUpdateDAO;
import sk.uniba.grman19.models.entity.Source;
import sk.uniba.grman19.models.entity.SourceUpdate;
import sk.uniba.grman19.models.entity.SourceUpdate_;

@Component
@Transactional(readOnly = true)
public class SourceUpdateDAOImpl implements SourceUpdateDAO {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@Transactional(readOnly = false)
	public void saveSourceUpdate(Source source, String value, Date date) {
		entityManager.persist(new SourceUpdate(source, value, date));
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
}
