package sk.uniba.grman19.dao;

import sk.uniba.grman19.models.MailSettings;
import sk.uniba.grman19.models.UWSUser;

public interface MailSettingsDAO {
	MailSettings createMailSettings(UWSUser user, String email);
}
