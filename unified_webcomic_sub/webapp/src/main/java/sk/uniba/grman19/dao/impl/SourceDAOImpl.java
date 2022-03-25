package sk.uniba.grman19.dao.impl;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import sk.uniba.grman19.dao.SourceDAO;
import sk.uniba.grman19.models.entity.Source;
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
	public List<Source> getAllSources() {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Source> cq = cb.createQuery(Source.class);
		Root<Source> root = cq.from(Source.class);
		cq.select(root);
		return entityManager.createQuery(cq).getResultList();
	}

}
