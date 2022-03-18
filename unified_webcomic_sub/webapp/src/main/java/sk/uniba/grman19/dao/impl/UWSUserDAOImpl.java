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

import sk.uniba.grman19.dao.UWSUserDAO;
import sk.uniba.grman19.models.UWSUser;
import sk.uniba.grman19.models.UWSUser_;

@Component
@Transactional(readOnly = true)
public class UWSUserDAOImpl implements UWSUserDAO {
	@Autowired
	private EntityManager entityManager;

	@Override
	public Optional<UWSUser> getUser(String name) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<UWSUser> cq = cb.createQuery(UWSUser.class);
		Root<UWSUser> root = cq.from(UWSUser.class);
		cq.select(root);
		cq.where(cb.equal(root.get(UWSUser_.name), cb.literal(name)));
		List<UWSUser>s= entityManager.createQuery(cq).getResultList();
		if (s.isEmpty()) {
			return Optional.empty();
		} else {
			return Optional.of(s.get(0)); 
		}
	}

}
