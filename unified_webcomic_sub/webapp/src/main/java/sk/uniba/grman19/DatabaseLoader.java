package sk.uniba.grman19;

import static sk.uniba.grman19.processing.Constants.SIMPLE_POLL;
import static sk.uniba.grman19.processing.Constants.SIMPLE_POLL_ATTRIBUTE;
import static sk.uniba.grman19.processing.Constants.SIMPLE_POLL_CHECK;
import static sk.uniba.grman19.processing.Constants.SIMPLE_POLL_TAG;
import static sk.uniba.grman19.processing.Constants.SIMPLE_POLL_URL;
import static sk.uniba.grman19.processing.Constants.SOURCE_TYPE;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import sk.uniba.grman19.dao.SeenUpdateDAO;
import sk.uniba.grman19.dao.SourceAttributeDAO;
import sk.uniba.grman19.dao.SourceDAO;
import sk.uniba.grman19.dao.SourceUpdateDAO;
import sk.uniba.grman19.dao.SubGroupDAO;
import sk.uniba.grman19.dao.SubscriptionDAO;
import sk.uniba.grman19.dao.UWSUserDAO;
import sk.uniba.grman19.models.entity.MailSettings;
import sk.uniba.grman19.models.entity.Source;
import sk.uniba.grman19.models.entity.SourceUpdate;
import sk.uniba.grman19.models.entity.SubGroup;
import sk.uniba.grman19.models.entity.UWSUser;
import sk.uniba.grman19.models.rest.MailSettingUpdate;
import sk.uniba.grman19.models.rest.UserRegistration;
import sk.uniba.grman19.service.MailSettingsService;
import sk.uniba.grman19.service.PostService;
import sk.uniba.grman19.service.UWSUserService;

@Component
@Profile("dev & !heroku")
public class DatabaseLoader implements CommandLineRunner {

	@Autowired
	private SourceDAO sourceDao;
	@Autowired
	private UWSUserDAO userDao;
	@Autowired
	private UWSUserService userService;
	@Autowired
	private SubGroupDAO subGroupDao;
	@Autowired
	private SubscriptionDAO subscriptionDao;
	@Autowired
	private SourceAttributeDAO sourceAttributeDao;
	@Autowired
	private SourceUpdateDAO sourceUpdateDao;
	@Autowired
	private MailSettingsService mailSettingsService;
	@Autowired
	private SeenUpdateDAO seenUpdateDao;
	@Autowired
	private PostService postService;

	// some test data
	@Override
	public void run(String... strings) throws Exception {
		Date date = new Date();
		Date old = new Date(365*24*60*60*1000l);
		// create some groups and sources
		List<Source> sources = IntStream.range(0, 20)
			.mapToObj(this::createSource)
			.collect(Collectors.toList());
		List<SubGroup> groups = IntStream.range(0, 10)
			.mapToObj(this::createGroup)
			.collect(Collectors.toList());
		// add some group relations
		subscriptionDao.addGroupRelation(groups.get(0), groups.get(1));
		subscriptionDao.addGroupRelation(groups.get(0), groups.get(2));
		subscriptionDao.addGroupRelation(groups.get(0), groups.get(3));
		subscriptionDao.addGroupRelation(groups.get(3), groups.get(4));
		subscriptionDao.addGroupRelation(groups.get(3), groups.get(5));
		subscriptionDao.addGroupRelation(groups.get(3), groups.get(6));
		// create a couple users
		UserRegistration user;
		UWSUser uuser;
		user = makeUserRegistration("root", "usbw", "root@root");
		uuser = userService.registerUser(user);
		// make root an owner
		uuser.setOwner(true);
		uuser = userDao.saveUWSUser(uuser);
		subscribe(uuser, groups.get(0));
		user = makeUserRegistration("user", "user", "user@root");
		uuser = userService.registerUser(user);
		// add some sources to the first group
		subscriptionDao.addSourceSubscription(groups.get(0), sources.get(0));
		subscriptionDao.addSourceSubscription(groups.get(0), sources.get(2));
		subscriptionDao.addSourceSubscription(groups.get(0), sources.get(4));
		// make 2 identical simple poll sources
		Map<String, String> attrs = new HashMap<String, String>();
		attrs.put(SOURCE_TYPE, SIMPLE_POLL);
		attrs.put(SIMPLE_POLL_URL, "https://davinci.fmph.uniba.sk/~grman19/uws/test.html");
		attrs.put(SIMPLE_POLL_TAG, "a");
		attrs.put(SIMPLE_POLL_CHECK, "^permalink.*$");
		attrs.put(SIMPLE_POLL_ATTRIBUTE, "href");
		addSourceAttrs(sources.get(0), attrs);
		addSourceAttrs(sources.get(1), attrs);
		// make 1 fancy poll source - cannot be processed
		addSourceAttrs(sources.get(2), Collections.singletonMap("type", "fancy_poll"));
		// add some old updates
		sourceUpdateDao.saveSourceUpdate(sources.get(0), "link0", date);
		sourceUpdateDao.saveSourceUpdate(sources.get(2), "somelink", date);

		UWSUser user1 = userService.registerUser(makeUserRegistration("name1", "password1", "mail1@address"));
		UWSUser user2 = userService.registerUser(makeUserRegistration("name2", "password2", "mail2@address"));
		UWSUser user3 = userService.registerUser(makeUserRegistration("name3", "password3", "mail3@address"));
		UWSUser user4 = userService.registerUser(makeUserRegistration("name4", "password4", "mail4@address"));
		UWSUser user5 = userService.registerUser(makeUserRegistration("name5", "password5", "mail5@address"));
		mailSettingsService.updateUserMailSettings(user1, makeMailUpdate(true, false, 1));
		mailSettingsService.updateUserMailSettings(user2, makeMailUpdate(false, true, 1));
		mailSettingsService.updateUserMailSettings(user3, makeMailUpdate(true, true, 2));
		mailSettingsService.updateUserMailSettings(user4, makeMailUpdate(true, true, 3));
		mailSettingsService.updateUserMailSettings(user5, makeMailUpdate(true, true, 4));
		// user1&2: direct subs only
		subscribe(user1, sources.get(10));
		subscribe(user1, sources.get(11));
		subscribe(user2, sources.get(10));
		subscribe(user2, sources.get(11));
		subscribe(user1, user2);
		// user3: direct and indirect subs
		subscribe(user3, groups.get(0));
		ignore(user3, sources.get(4));
		subscribe(user3, sources.get(10));
		subscribe(user3, sources.get(11));
		// user4: cycle in groups - does not play nice with H2
		/*subscribe(user3, groups.get(8));
		subscriptionDao.addGroupRelation(groups.get(8), groups.get(9));
		subscriptionDao.addGroupRelation(groups.get(9), groups.get(8));
		subscriptionDao.addSourceSubscription(groups.get(9), sources.get(11));/**/
		// user5: none
		// add some updates, user3 has seen one of them
		sourceUpdateDao.saveSourceUpdate(sources.get(10), "link10", date);
		SourceUpdate update = sourceUpdateDao.saveSourceUpdate(sources.get(11), "link11", old);
		seenUpdateDao.createSeenUpdate(user3, update);
		sourceUpdateDao.saveSourceUpdate(sources.get(11), "link12", date);

		postService.createPost(user2, "Post 1", "Content 1", Arrays.asList("opt1", "opt2"));
		postService.createPost(user2, "Post 2", "Content 2", Collections.emptyList());
	}

	private void addSourceAttrs(Source source, Map<String, String> attrs) {
		for (Entry<String, String> attr : attrs.entrySet()) {
			sourceAttributeDao.addAttribute(source, attr.getKey(), attr.getValue());
		}
	}

	private void subscribe(UWSUser uuser, SubGroup group) {
		MailSettings ms = userDao.getUser(uuser.getId())
			.get()
			.getMailSettings();
		subscriptionDao.addGroupRelation(ms.getSubscribe(), group);
	}

	private void subscribe(UWSUser uuser, Source source) {
		MailSettings ms = userDao.getUser(uuser.getId())
			.get()
			.getMailSettings();
		subscriptionDao.addSourceSubscription(ms.getSubscribe(), source);
	}

	private void subscribe(UWSUser uuser, UWSUser poster) {
		MailSettings ms = userDao.getUser(uuser.getId())
			.get()
			.getMailSettings();
		subscriptionDao.addPosterSubscription(ms.getSubscribe(), poster);
	}

	private void ignore(UWSUser uuser, Source source) {
		MailSettings ms = userDao.getUser(uuser.getId())
			.get()
			.getMailSettings();
		subscriptionDao.addSourceSubscription(ms.getIgnore(), source);
	}

	private Source createSource(int number) {
		return sourceDao.createSource("source" + number, "descr" + number);
	}

	private SubGroup createGroup(int number) {
		return subGroupDao.createPublicGroup("group" + number, "descr" + number);
	}

	private UserRegistration makeUserRegistration(String name, String pass, String email) {
		UserRegistration user = new UserRegistration();
		user.setUsername(name);
		user.setPassword(pass);
		user.setEmail(email);
		return user;
	}

	private static MailSettingUpdate makeMailUpdate(boolean daily, boolean weekly, int dayOfWeek) {
		MailSettingUpdate msu = new MailSettingUpdate();
		msu.setDaily(daily);
		msu.setWeekly(weekly);
		msu.setDayOfWeek((byte) dayOfWeek);
		return msu;
	}
}