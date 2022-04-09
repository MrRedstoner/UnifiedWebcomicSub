package sk.uniba.grman19.dao.impl;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import sk.uniba.grman19.dao.UWSUserDAO;
import sk.uniba.grman19.models.entity.UWSUser;
import sk.uniba.grman19.models.entity.UWSUser_;
import sk.uniba.grman19.util.query.SimpleQuery;

@Component
@Transactional(readOnly = true)
public class UWSUserDAOImpl implements UWSUserDAO {

	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	@Qualifier("userQueryByName")
	private SimpleQuery<UWSUser, String> queryByName;

	@Override
	public Optional<UWSUser> getUser(Long id) {
		return Optional.ofNullable(entityManager.find(UWSUser.class, id));
	}

	@Override
	public Optional<UWSUser> getUser(String name) {
		return queryByName.querySingle(name);
	}

	@Override
	@Transactional(readOnly = false)
	public UWSUser createUWSUser(UWSUser user) {
		if (getUser(user.getName()).isPresent()) {
			throw new IllegalArgumentException("Name already used");
		}
		entityManager.persist(user);
		return user;
	}

	@Override
	@Transactional(readOnly = false)
	public UWSUser saveUWSUser(UWSUser user) {
		return entityManager.merge(user);
	}

	private static Predicate nameEqual(CriteriaBuilder cb, Root<UWSUser> root, String name) {
		return cb.equal(root.get(UWSUser_.name), cb.literal(name));
	}

	@Configuration
	static class Config {
		@Bean(name = "userQueryByName")
		@PersistenceContext
		SimpleQuery<UWSUser, String> queryByName(EntityManager entityManager) {
			return new SimpleQuery<>(entityManager, UWSUser.class, UWSUserDAOImpl::nameEqual);
		}
	}
}
