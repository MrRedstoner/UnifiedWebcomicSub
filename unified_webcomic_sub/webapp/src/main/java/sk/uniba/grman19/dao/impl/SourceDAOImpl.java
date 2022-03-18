package sk.uniba.grman19.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;

import org.springframework.beans.factory.annotation.Autowired;

import sk.uniba.grman19.dao.SourceDAO;
import sk.uniba.grman19.models.Source;

public class SourceDAOImpl implements SourceDAO {
	@Autowired
	private EntityManager entityManager;

	//@Transactional(readOnly=true)
	@Override
	public Source getSource(int id) {
		// TODO Auto-generated method stub
		CriteriaBuilder cb=entityManager.getCriteriaBuilder();
		//cb.createQuery(Source.class).select(Source)
		return null;
	}

}
