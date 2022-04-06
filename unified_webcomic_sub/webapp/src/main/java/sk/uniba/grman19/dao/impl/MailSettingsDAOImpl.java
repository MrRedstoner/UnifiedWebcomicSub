package sk.uniba.grman19.dao.impl;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import sk.uniba.grman19.dao.MailSettingsDAO;
import sk.uniba.grman19.dao.SubGroupDAO;
import sk.uniba.grman19.models.entity.MailSettings;
import sk.uniba.grman19.models.entity.SubGroup;
import sk.uniba.grman19.models.entity.UWSUser;

@Component
@Transactional(readOnly = true)
public class MailSettingsDAOImpl implements MailSettingsDAO {
	private static final Date DATE_NEVER = new Date(0l);

	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	private SubGroupDAO subGroupDao;

	@Override
	@Transactional(readOnly = false)
	public MailSettings createMailSettings(UWSUser user, String email) {
		SubGroup subscribe = subGroupDao.createUserGroup();
		SubGroup ignore = subGroupDao.createUserGroup();
		MailSettings settings = new MailSettings(user, email, subscribe, ignore, false, false, (byte) 0, DATE_NEVER, DATE_NEVER);
		entityManager.persist(settings);
		return settings;
	}

	@Override
	@Transactional(readOnly = false)
	public void saveMailSettings(MailSettings settings) {
		entityManager.merge(settings);
	}
}
