package sk.uniba.grman19.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import sk.uniba.grman19.dao.SourceAttributeDAO;
import sk.uniba.grman19.models.entity.Source;
import sk.uniba.grman19.models.entity.SourceAttribute;

@Component
@Transactional(readOnly = true)
public class SourceAttributeDAOImpl implements SourceAttributeDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@Transactional(readOnly = false)
	public SourceAttribute addAttribute(Source source, String key, String value) {
		// TODO check for dupes/run from diff etc.
		SourceAttribute newAttribute = new SourceAttribute(source, key, value);
		entityManager.persist(newAttribute);
		return newAttribute;
	}

}
