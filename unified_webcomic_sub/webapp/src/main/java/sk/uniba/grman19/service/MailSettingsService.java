package sk.uniba.grman19.service;

import static sk.uniba.grman19.util.ConversionUtils.getIds;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sk.uniba.grman19.dao.AuditLogDAO;
import sk.uniba.grman19.dao.MailSettingsDAO;
import sk.uniba.grman19.dao.UWSUserDAO;
import sk.uniba.grman19.models.entity.MailSettings;
import sk.uniba.grman19.models.entity.UWSUser;
import sk.uniba.grman19.models.rest.MailSettingUpdate;

@Service
public class MailSettingsService {

	@Autowired
	private MailSettingsDAO mailSettingsDao;
	@Autowired
	private UWSUserDAO userDao;
	@Autowired
	private AuditLogDAO auditLogDao;

	@Transactional(readOnly = false)
	public void updateUserMailSettings(UWSUser user, MailSettingUpdate updates) {
		// fetch fresh for transaction
		user = userDao.getUser(user.getId()).get();

		MailSettings settings = user.getMailSettings();
		Optional<MailSettingUpdate> omsu = Optional.of(updates);
		List<String> changes = new LinkedList<>();

		omsu.map(MailSettingUpdate::getMailAddress)
			.map(addChange("mailAddress", changes))
			.ifPresent(settings::setMailAddress);
		omsu.map(MailSettingUpdate::getDaily)
			.map(addChange("daily", changes))
			.ifPresent(settings::setDaily);
		omsu.map(MailSettingUpdate::getWeekly)
			.map(addChange("weekly", changes))
			.ifPresent(settings::setWeekly);
		omsu.map(MailSettingUpdate::getDayOfWeek)
			.map(addChange("dayOfWeek", changes))
			.ifPresent(settings::setDayOfWeek);

		auditLogDao.saveLog(user, "Updated mail settings " + changes.toString());
		mailSettingsDao.saveMailSettings(settings);
	}

	public List<MailSettings> getActiveDailyMail(Date today) {
		return mailSettingsDao.getActiveDailyMail(today);
	}

	public void saveLastDaily(List<MailSettings> usersSent, Date date) {
		mailSettingsDao.updateLastDaily(getIds(usersSent), date);
	}

	public List<MailSettings> getActiveWeeklyMail(Byte dayOfWeek, Date today) {
		return mailSettingsDao.getActiveWeeklyMail(dayOfWeek, today);
	}

	public void saveLastWeekly(List<MailSettings> usersSent, Date date) {
		mailSettingsDao.updateLastWeekly(getIds(usersSent), date);
	}

	private <O> Function<O, O> addChange(String fieldName, List<String> list) {
		return o -> {
			list.add(fieldName + "=" + o.toString());
			return o;
		};
	}
}
