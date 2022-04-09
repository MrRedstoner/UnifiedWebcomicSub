package sk.uniba.grman19;

import static sk.uniba.grman19.processing.Constants.SIMPLE_POLL;
import static sk.uniba.grman19.processing.Constants.SIMPLE_POLL_ATTRIBUTE;
import static sk.uniba.grman19.processing.Constants.SIMPLE_POLL_CHECK;
import static sk.uniba.grman19.processing.Constants.SIMPLE_POLL_TAG;
import static sk.uniba.grman19.processing.Constants.SIMPLE_POLL_URL;
import static sk.uniba.grman19.processing.Constants.SOURCE_TYPE;

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
import org.springframework.stereotype.Component;

import sk.uniba.grman19.dao.SourceAttributeDAO;
import sk.uniba.grman19.dao.SourceDAO;
import sk.uniba.grman19.dao.SourceUpdateDAO;
import sk.uniba.grman19.dao.SubGroupDAO;
import sk.uniba.grman19.dao.SubscriptionDAO;
import sk.uniba.grman19.dao.UWSUserDAO;
import sk.uniba.grman19.models.entity.MailSettings;
import sk.uniba.grman19.models.entity.Source;
import sk.uniba.grman19.models.entity.SubGroup;
import sk.uniba.grman19.models.entity.UWSUser;
import sk.uniba.grman19.models.rest.UserRegistration;
import sk.uniba.grman19.service.UWSUserService;

@Component
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

	// some test data
	@Override
	public void run(String... strings) throws Exception {
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
		sourceUpdateDao.saveSourceUpdate(sources.get(0), "link0", new Date());
		sourceUpdateDao.saveSourceUpdate(sources.get(2), "somelink", new Date());
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
}