package sk.uniba.grman19.dao.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import sk.uniba.grman19.dao.AuditLogDAO;
import sk.uniba.grman19.models.entity.AuditLog;
import sk.uniba.grman19.models.entity.UWSUser;
import sk.uniba.grman19.repository.AuditLogRepository;

@Component
@Transactional(readOnly = true)
public class AuditLogDAOImpl implements AuditLogDAO {
	@Autowired
	private AuditLogRepository repository;

	@Override
	@Transactional(readOnly = false)
	public void saveLog(UWSUser user, String message) {
		repository.save(new AuditLog(user, new Date(), message));
	}

}
