package sk.uniba.grman19.service;

import static sk.uniba.grman19.util.ConversionUtils.toDayOfWeek;
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
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.javatuples.Pair;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sk.uniba.grman19.dao.PostDAO;
import sk.uniba.grman19.dao.SourceDAO;
import sk.uniba.grman19.dao.SourceUpdateDAO;
import sk.uniba.grman19.dao.UWSUserDAO;
import sk.uniba.grman19.models.entity.MailSettings;
import sk.uniba.grman19.models.entity.Post;
import sk.uniba.grman19.models.entity.Source;
import sk.uniba.grman19.models.entity.SourceUpdate;
import sk.uniba.grman19.models.entity.UWSUser;
import sk.uniba.grman19.processing.mail.EmailSender;
import sk.uniba.grman19.util.ConversionUtils;

@Service
public class MailSendingService {
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	// TODO bean?
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

	@Autowired
	private SourceDAO sourceDao;
	@Autowired
	private UWSUserDAO userDao;
	@Autowired
	private SourceUpdateDAO sourceUpdateDao;
	@Autowired
	private PostDAO postDao;
	@Autowired
	private EmailSender emailSender;
	@Autowired
	private MailSettingsService mailSettingsService;
	@Autowired
	private SeenUpdateService seenUpdateService;
	@Autowired
	private SeenPostService seenPostService;

	@Transactional(readOnly = false)
	public void sendDailyMail() {
		sendRegularMail("UWS Daily", ld -> mailSettingsService.getActiveDailyMail(toUtilDate(ld)), mailSettingsService::saveLastDaily);
	}

	@Transactional(readOnly = false)
	public void sendWeeklyMail() {
		sendRegularMail("UWS Weekly", ld -> mailSettingsService.getActiveWeeklyMail(toDayOfWeek(ld), toUtilDate(ld)), mailSettingsService::saveLastWeekly);
	}

	private void sendRegularMail(String subject, Function<LocalDate, List<MailSettings>> getUsers, BiConsumer<List<MailSettings>, Date> updateSent) {
		LocalDate now = LocalDate.now();
		Date today = toUtilDate(now);
		List<MailSettings> users = getUsers.apply(now);
		List<MailSettings> usersSent = new ArrayList<>();
		logger.debug(users.stream().map(MailSettings::getMailAddress).collect(Collectors.joining(", ")));
		for (MailSettings settings : users) {
			try {
				Pair<List<SourceUpdate>, List<Post>> sent = sendMail(settings, subject);
				if (sent.getValue0().isEmpty() && sent.getValue1().isEmpty()) {
					// no mail was sent - maybe later same day, skip
				} else {
					usersSent.add(settings);
					seenUpdateService.updateSeenUpdates(settings.getUser(), sent.getValue0());
					seenPostService.updateSeenPosts(settings.getUser(), sent.getValue1());
				}
			} catch (Exception e) {
				logger.error("Error sending mail", e);
			}
		}
		if (!usersSent.isEmpty()) {
			updateSent.accept(usersSent, today);
		}
	}

	private List<SourceUpdate> getSourceUpdates(MailSettings settings){
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
		return updates;
	}

	private List<Post> getPosts(MailSettings settings){
		List<UWSUser> posters = userDao.resolvePosters(settings.getSubscribe(), settings.getIgnore());
		if (posters.isEmpty()) {
			return Collections.emptyList();
		}
		logger.debug(posters.stream().map(UWSUser::getName).collect(Collectors.joining(", ")));

		Set<Long> posterIds = ConversionUtils.getIds(posters);
		List<Post> posts = postDao.getUnseenPosts(settings.getUser().getId(), posterIds);
		if (posts.isEmpty()) {
			return Collections.emptyList();
		}
		return posts;
	}

	@Transactional(readOnly = false)
	private Pair<List<SourceUpdate>, List<Post>> sendMail(MailSettings settings, String subject) throws Exception {
		List<SourceUpdate> updates = getSourceUpdates(settings);
		List<Post> posts = getPosts(settings);
		if (updates.isEmpty() && posts.isEmpty()) {
			return Pair.with(Collections.emptyList(), Collections.emptyList());
		}

		Document doc = Document.createShell("");

		if(!updates.isEmpty()){
			Element table = doc.createElement("table");
			table.appendChild(createSourceHeader(doc));
			for (SourceUpdate update : updates) {
				table.appendChild(formatUpdate(doc, update));
			}
			Element title = doc.createElement("h2").appendText("Source updates");
			doc.body()
				.appendChild(title)
				.appendChild(table);
		}

		if(!posts.isEmpty()){
			Element table = doc.createElement("table");
			table.appendChild(createPostHeader(doc));
			for (Post post : posts) {
				table.appendChild(formatUpdate(doc, post));
			}
			Element title = doc.createElement("h2").appendText("Posts");
			doc.body()
				.appendChild(title)
				.appendChild(table);
		}

		emailSender.sendHtmlMail(settings.getMailAddress(), subject, doc);

		return Pair.with(updates, posts);
	}

	private Element createSourceHeader(Document doc) {
		Element row = doc.createElement("tr");
		row.appendChild(doc.createElement("th").appendText("Source"));
		row.appendChild(doc.createElement("th").appendText("Update value"));
		row.appendChild(doc.createElement("th").appendText("Update time"));
		return row;
	}

	private Element createPostHeader(Document doc) {
		Element row = doc.createElement("tr");
		row.appendChild(doc.createElement("th").appendText("Poster"));
		row.appendChild(doc.createElement("th").appendText("Title"));
		row.appendChild(doc.createElement("th").appendText("Posted"));
		return row;
	}

	private Element formatUpdate(Document doc, SourceUpdate update) {
		Element row = doc.createElement("tr");
		row.appendChild(doc.createElement("td").appendText(update.getSource().getName()));
		row.appendChild(doc.createElement("td").appendText(update.getValue()));
		row.appendChild(doc.createElement("td").appendText(DATE_FORMAT.format(update.getUpdateTime())));
		return row;
	}

	private Element formatUpdate(Document doc, Post post) {
		Element row = doc.createElement("tr");
		row.appendChild(doc.createElement("td").appendText(post.getUser().getName()));
		row.appendChild(doc.createElement("td").appendText(post.getTitle()));
		row.appendChild(doc.createElement("td").appendText(DATE_FORMAT.format(post.getCreated())));
		return row;
	}
}
