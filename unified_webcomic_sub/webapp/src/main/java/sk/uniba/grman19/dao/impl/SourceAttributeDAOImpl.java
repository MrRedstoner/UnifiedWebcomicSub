package sk.uniba.grman19.dao.impl;

import static sk.uniba.grman19.util.FunctionUtils.mapLast;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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

import sk.uniba.grman19.dao.SourceAttributeDAO;
import sk.uniba.grman19.models.entity.Source;
import sk.uniba.grman19.models.entity.SourceAttribute;
import sk.uniba.grman19.models.entity.SourceAttribute_;
import sk.uniba.grman19.util.query.SimpleQuery;

@Component
@Transactional(readOnly = true)
public class SourceAttributeDAOImpl implements SourceAttributeDAO {
	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	@Qualifier("attributeQueryBySourceName")
	SimpleQuery<SourceAttribute, Pair<Long, String>> queryBySourceName;

	@Override
	public void updateAttributes(Source source, Map<String, String> create, Map<String, String> update, Set<String> delete) {
		for (Entry<String, String> attr : create.entrySet()) {
			addAttribute(source, attr.getKey(), attr.getValue());
		}

		for (Entry<String, String> attr : update.entrySet()) {
			updateAttribute(source, attr.getKey(), attr.getValue());
		}

		for (String name : delete) {
			deleteAttribute(source, name);
		}

		entityManager.flush();
		entityManager.clear();
	}

	@Override
	@Transactional(readOnly = false)
	public SourceAttribute addAttribute(Source source, String key, String value) {
		SourceAttribute newAttribute = new SourceAttribute(source, key, value);
		entityManager.persist(newAttribute);
		return newAttribute;
	}

	@Override
	@Transactional(readOnly = false)
	public SourceAttribute updateAttribute(Source source, String name, String value) {
		SourceAttribute attribute = queryBySourceName.querySingle(Pair.with(source.getId(), name)).get();
		attribute.setValue(value);
		return entityManager.merge(attribute);
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteAttribute(Source source, String name) {
		queryBySourceName.executeDelete(Pair.with(source.getId(), name));
	}

	private static Predicate sourceIdEqual(CriteriaBuilder cb, Root<SourceAttribute> root, Long sourceId) {
		return cb.equal(root.get(SourceAttribute_.source), cb.literal(sourceId));
	}

	private static Predicate nameEqual(CriteriaBuilder cb, Root<SourceAttribute> root, String name) {
		return cb.equal(root.get(SourceAttribute_.name), cb.literal(name));
	}

	@Configuration
	static class Config {
		@Bean(name = "attributeQueryBySourceName")
		@PersistenceContext
		SimpleQuery<SourceAttribute, Pair<Long, String>> queryBySourceName(EntityManager entityManager) {
			return new SimpleQuery<>(entityManager, SourceAttribute.class, mapLast(Pair::getValue0, SourceAttributeDAOImpl::sourceIdEqual),
					mapLast(Pair::getValue1, SourceAttributeDAOImpl::nameEqual));
		}
	}
}
