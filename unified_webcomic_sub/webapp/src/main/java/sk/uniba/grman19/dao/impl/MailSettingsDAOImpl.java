package sk.uniba.grman19.dao.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import sk.uniba.grman19.dao.MailSettingsDAO;
import sk.uniba.grman19.dao.SubGroupDAO;
import sk.uniba.grman19.models.entity.MailSettings;
import sk.uniba.grman19.models.entity.SubGroup;
import sk.uniba.grman19.models.entity.UWSUser;
import sk.uniba.grman19.repository.MailSettingsRepository;

@Component
@Transactional(readOnly = true)
public class MailSettingsDAOImpl implements MailSettingsDAO {
	private static final Date DATE_NEVER = new Date(0l);
	@Autowired
	private MailSettingsRepository repository;
	@Autowired
	private SubGroupDAO subGroupDao;

	@Transactional(readOnly = false)
	@Override
	public MailSettings createMailSettings(UWSUser user, String email) {
		SubGroup subscribe = subGroupDao.createUserGroup();
		SubGroup ignore = subGroupDao.createUserGroup();
		MailSettings ret = repository.save(new MailSettings(user, email, subscribe, ignore, false, false, (byte) 0, DATE_NEVER, DATE_NEVER));
		return ret;
	}

	@Transactional(readOnly = false)
	@Override
	public void saveMailSettings(MailSettings settings) {
		repository.save(settings);
	}

}
