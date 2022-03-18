package sk.uniba.grman19.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import sk.uniba.grman19.dao.SourceDAO;
import sk.uniba.grman19.models.Source;
import sk.uniba.grman19.models.Source_;

@Component
public class SourceDAOImpl implements SourceDAO {
	@Autowired
	private EntityManager entityManager;

	@Transactional(readOnly = true)
	@Override
	public Source getSource(int id) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Source> cq = cb.createQuery(Source.class);
		Root<Source> root = cq.from(Source.class);
		cq.select(root);
		cq.where(cb.equal(root.get(Source_.id), cb.literal(id)));
		return entityManager.createQuery(cq).getResultList().get(0);
	}

	@Override
	public List<Source> getAllSources() {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Source> cq = cb.createQuery(Source.class);
		Root<Source> root = cq.from(Source.class);
		cq.select(root);
		return entityManager.createQuery(cq).getResultList();
	}

}
