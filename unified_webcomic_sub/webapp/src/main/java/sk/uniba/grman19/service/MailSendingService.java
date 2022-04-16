package sk.uniba.grman19.service;

import static sk.uniba.grman19.util.ConversionUtils.toUtilDate;

import java.lang.invoke.MethodHandles;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sk.uniba.grman19.dao.SourceDAO;
import sk.uniba.grman19.dao.SourceUpdateDAO;
import sk.uniba.grman19.models.entity.MailSettings;
import sk.uniba.grman19.models.entity.Source;
import sk.uniba.grman19.models.entity.SourceUpdate;
import sk.uniba.grman19.processing.mail.EmailSender;

@Service
public class MailSendingService {
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	// TODO bean?
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

	@Autowired
	private SourceDAO sourceDao;
	@Autowired
	private SourceUpdateDAO sourceUpdateDao;
	@Autowired
	private EmailSender emailSender;
	@Autowired
	private MailSettingsService mailSettingsService;
	@Autowired
	private SeenUpdateService seenUpdateService;

	@Transactional(readOnly = false)
	public void sendDailyMail() {
		Date today = toUtilDate(LocalDate.now());
		List<MailSettings> users = mailSettingsService.getActiveDailyMail(today);
		List<MailSettings> usersSent = new ArrayList<>();
		logger.debug(users.stream().map(MailSettings::getMailAddress).collect(Collectors.joining(", ")));
		for (MailSettings settings : users) {
			try {
				List<SourceUpdate> sent = sendMail(settings);
				// TODO only update if nonempty, update last sent
				if (!sent.isEmpty()) {
					usersSent.add(settings);
					seenUpdateService.updateSeenUpdates(settings.getUser(), sent);
				}
			} catch (Exception e) {
				logger.error("Error sending mail", e);
			}
		}
		if (!usersSent.isEmpty()) {
			mailSettingsService.saveLastDaily(usersSent, today);
		}
	}

	@Transactional(readOnly = false)
	private List<SourceUpdate> sendMail(MailSettings settings) throws Exception {
		List<Source> sources = sourceDao.resolveSources(settings.getSubscribe(), settings.getIgnore());
		if (sources.isEmpty()) {
			return Collections.emptyList();
		}
		logger.debug(sources.stream().map(Source::getName).collect(Collectors.joining(", ")));

		Set<Long> sourceIds = sources.stream()
			.map(Source::getId)
			.collect(Collectors.toSet());
		List<SourceUpdate> updates = sourceUpdateDao.getUnseenUpdates(settings.getUser().getId(), sourceIds);
		if (updates.isEmpty()) {
			return Collections.emptyList();
		}

		Document doc = Document.createShell("");
		Element table = doc.createElement("table");
		table.appendChild(createHeader(doc));
		for (SourceUpdate update : updates) {
			table.appendChild(formatUpdate(doc, update));
		}
		doc.body().appendChild(table);

		emailSender.sendHtmlMail(settings.getMailAddress(), "UWS Daily", doc);

		return updates;
	}

	private Element createHeader(Document doc) {
		Element row = doc.createElement("tr");
		row.appendChild(doc.createElement("th").appendText("Source"));
		row.appendChild(doc.createElement("th").appendText("Update value"));
		row.appendChild(doc.createElement("th").appendText("Update time"));
		return row;
	}

	private Element formatUpdate(Document doc, SourceUpdate update) {
		Element row = doc.createElement("tr");
		row.appendChild(doc.createElement("td").appendText(update.getSource().getName()));
		row.appendChild(doc.createElement("td").appendText(update.getValue()));
		row.appendChild(doc.createElement("td").appendText(DATE_FORMAT.format(update.getUpdateTime())));
		return row;
	}
}
