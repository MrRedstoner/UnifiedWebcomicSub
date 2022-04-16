package sk.uniba.grman19.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import sk.uniba.grman19.models.entity.MailSettings;
import sk.uniba.grman19.models.entity.UWSUser;

public interface MailSettingsDAO {
	MailSettings createMailSettings(UWSUser user, String email);

	void saveMailSettings(MailSettings settings);

	List<MailSettings> getActiveDailyMail(Date today);

	void updateLastDaily(Collection<Long> usersSent, Date date);
}
