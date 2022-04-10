package sk.uniba.grman19.dao.impl;

import static sk.uniba.grman19.util.FunctionUtils.mapLast;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import sk.uniba.grman19.dao.SeenUpdateDAO;
import sk.uniba.grman19.models.entity.SeenUpdate;
import sk.uniba.grman19.models.entity.SeenUpdate_;
import sk.uniba.grman19.models.entity.SourceUpdate;
import sk.uniba.grman19.models.entity.UWSUser;
import sk.uniba.grman19.util.query.SimpleQuery;

@Component
@Transactional(readOnly = true)
public class SeenUpdateDAOImpl implements SeenUpdateDAO {

	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	@Qualifier("seenUpdateQueryByUserUpdate")
	private SimpleQuery<SeenUpdate, Pair<Long, Long>> querySourceByIds;

	@Override
	@Transactional(readOnly = false)
	public SeenUpdate createSeenUpdate(UWSUser user, SourceUpdate update) {
		SeenUpdate seen = new SeenUpdate(user, update);
		entityManager.persist(seen);
		return seen;
	}

	@Override
	@Transactional(readOnly = false)
	public SeenUpdate updateSeenUpdate(UWSUser user, SourceUpdate update) {
		Optional<SeenUpdate> lastUpdate = querySourceByIds.querySingle(Pair.with(user.getId(), update.getId()));
		if (lastUpdate.isPresent()) {
			SeenUpdate seenUpdate = lastUpdate.get();
			seenUpdate.setUpdate(update);
			return entityManager.merge(seenUpdate);
		}
		return createSeenUpdate(user, update);
	}

	private static Predicate userEqual(CriteriaBuilder cb, Root<SeenUpdate> root, Long user) {
		return cb.equal(root.get(SeenUpdate_.user), cb.literal(user));
	}

	private static Predicate sourceUpdateEqual(CriteriaBuilder cb, Root<SeenUpdate> root, Long update) {
		return cb.equal(root.get(SeenUpdate_.update), cb.literal(update));
	}

	@Configuration
	static class Config {
		@Bean(name = "seenUpdateQueryByUserUpdate")
		@PersistenceContext
		SimpleQuery<SeenUpdate, Pair<Long, Long>> queryByName(EntityManager entityManager) {
			return new SimpleQuery<>(entityManager, SeenUpdate.class, mapLast(Pair::getValue0, SeenUpdateDAOImpl::userEqual), mapLast(Pair::getValue1, SeenUpdateDAOImpl::sourceUpdateEqual));
		}
	}
}
