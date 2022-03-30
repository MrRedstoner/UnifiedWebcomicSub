package sk.uniba.grman19.dao.impl;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import sk.uniba.grman19.dao.UWSUserDAO;
import sk.uniba.grman19.models.entity.UWSUser;
import sk.uniba.grman19.models.entity.UWSUser_;
import sk.uniba.grman19.repository.UWSUserRepository;
import sk.uniba.grman19.util.query.SimpleQuery;

@Component
@Transactional(readOnly = true)
public class UWSUserDAOImpl implements UWSUserDAO {

	@Autowired
	public UWSUserDAOImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
		this.queryByName = new SimpleQuery<>(entityManager, UWSUser.class, this::nameEqual);
	}

	@Autowired
	private UWSUserRepository userRepository;
	@SuppressWarnings("unused")
	private final EntityManager entityManager;
	private final SimpleQuery<UWSUser, String> queryByName;

	@Override
	public Optional<UWSUser> getUser(Long id) {
		return userRepository.findById(id);
	}

	@Override
	public Optional<UWSUser> getUser(String name) {
		return queryByName.querySingle(name);
	}

	@Transactional(readOnly = false)
	@Override
	public UWSUser createUWSUser(UWSUser user) {
		if (getUser(user.getName()).isPresent()) {
			throw new IllegalArgumentException("Name already used");
		}
		return userRepository.save(user);
	}

	private Predicate nameEqual(CriteriaBuilder cb, Root<UWSUser> root, String name) {
		return cb.equal(root.get(UWSUser_.name), cb.literal(name));
	}
}
