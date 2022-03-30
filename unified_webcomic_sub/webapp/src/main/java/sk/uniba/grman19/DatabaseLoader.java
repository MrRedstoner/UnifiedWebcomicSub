package sk.uniba.grman19;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import sk.uniba.grman19.dao.SubGroupDAO;
import sk.uniba.grman19.dao.SubscriptionDAO;
import sk.uniba.grman19.models.entity.MailSettings;
import sk.uniba.grman19.models.entity.Source;
import sk.uniba.grman19.models.entity.SubGroup;
import sk.uniba.grman19.models.entity.UWSUser;
import sk.uniba.grman19.models.rest.UserRegistration;
import sk.uniba.grman19.repository.MailSettingsRepository;
import sk.uniba.grman19.repository.SourceRepository;
import sk.uniba.grman19.repository.UWSUserRepository;
import sk.uniba.grman19.service.UWSUserService;

@Component
public class DatabaseLoader implements CommandLineRunner {

	@Autowired
	private SourceRepository sourceRepository;
	@Autowired
	private UWSUserRepository userRepository;
	@Autowired
	private MailSettingsRepository mailRepository;
	@Autowired
	private UWSUserService userService;
	@Autowired
	private SubGroupDAO subGroupDao;
	@Autowired
	private SubscriptionDAO subscriptionDao;

	// some test data
	@Override
	public void run(String... strings) throws Exception {
		IntStream.range(0, 20)
			.forEach(this::createSource);
		List<SubGroup> groups = IntStream.range(0, 10)
			.mapToObj(this::createGroup)
			.collect(Collectors.toList());
		subscriptionDao.addGroupRelation(groups.get(0), groups.get(1));
		subscriptionDao.addGroupRelation(groups.get(0), groups.get(2));
		subscriptionDao.addGroupRelation(groups.get(0), groups.get(3));
		subscriptionDao.addGroupRelation(groups.get(3), groups.get(4));
		subscriptionDao.addGroupRelation(groups.get(3), groups.get(5));
		subscriptionDao.addGroupRelation(groups.get(3), groups.get(6));
		UserRegistration user;
		UWSUser uuser;
		user = makeUserRegistration("root", "usbw", "root@root");
		uuser = userService.registerUser(user);
		uuser.setOwner(true);
		userRepository.save(uuser);
		subscribe(uuser, groups.get(0));
		user = makeUserRegistration("user", "user", "user@root");
		uuser = userService.registerUser(user);
	}

	private void subscribe(UWSUser uuser, SubGroup group) {
		Long id = uuser.getId();
		id = userRepository.findById(id)
			.get()
			.getMailSettings()
			.getId();
		MailSettings ms = mailRepository.findById(id).get();
		subscriptionDao.addGroupRelation(ms.getSubscribe(), group);
	}

	private void createSource(int number) {
		sourceRepository.save(new Source("source" + number, "descr" + number));
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