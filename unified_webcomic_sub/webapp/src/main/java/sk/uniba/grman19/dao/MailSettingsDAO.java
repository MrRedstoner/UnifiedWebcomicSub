package sk.uniba.grman19.dao;

import sk.uniba.grman19.models.MailSettings;

public interface MailSettingsDAO {
	MailSettings createMailSettings(Long uid, String email);
}
