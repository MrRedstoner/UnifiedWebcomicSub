package sk.uniba.grman19.dao.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import sk.uniba.grman19.dao.MailSettingsDAO;
import sk.uniba.grman19.models.MailSettings;
import sk.uniba.grman19.repository.MailSettingsRepository;

@Component
public class MailSettingsDAOImpl implements MailSettingsDAO {
	private static final Date DATE_NEVER = new Date(0l);
	@Autowired
	private MailSettingsRepository repository;

	@Override
	public MailSettings createMailSettings(Long uid, String email) {
		MailSettings ret = repository.save(new MailSettings(uid, email, false, false, (byte) 0, DATE_NEVER, DATE_NEVER));
		return ret;
	}

}
