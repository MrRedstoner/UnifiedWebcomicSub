package sk.uniba.grman19.dao.impl;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import sk.uniba.grman19.dao.AuditLogDAO;
import sk.uniba.grman19.models.entity.AuditLog;
import sk.uniba.grman19.models.entity.UWSUser;

@Component
@Transactional(readOnly = true)
public class AuditLogDAOImpl implements AuditLogDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@Transactional(readOnly = false)
	public void saveLog(UWSUser user, String message) {
		entityManager.persist(new AuditLog(user, new Date(), message));
	}

}
