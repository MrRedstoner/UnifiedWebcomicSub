package sk.uniba.grman19;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import sk.uniba.grman19.models.Source;
import sk.uniba.grman19.models.UWSUser;
import sk.uniba.grman19.models.UserRegistration;
import sk.uniba.grman19.repository.MailSettingsRepository;
import sk.uniba.grman19.repository.SourceRepository;
import sk.uniba.grman19.repository.UWSUserRepository;
import sk.uniba.grman19.service.UWSUserService;

@Component
public class DatabaseLoader implements CommandLineRunner {

	@Autowired
	private SourceRepository sourceRepository;
	@SuppressWarnings("unused")
	@Autowired
	private MailSettingsRepository mailSettingRepository;
	@Autowired
	private UWSUserRepository userRepository;
	@Autowired
	private UWSUserService userService;

	// some test data
	@Override
	public void run(String... strings) throws Exception {
		sourceRepository.save(new Source("s1", "des1"));
		sourceRepository.save(new Source("s2", "des2"));
		sourceRepository.save(new Source("s3", "des3"));
		UserRegistration user;
		UWSUser uuser;
		user = makeUserRegistration("root", "usbw", "root@root");
		uuser = userService.registerUser(user);
		uuser.setOwner(true);
		userRepository.save(uuser);
		user = makeUserRegistration("user", "user", "user@root");
		uuser = userService.registerUser(user);
	}

	private UserRegistration makeUserRegistration(String name, String pass, String email) {
		UserRegistration user = new UserRegistration();
		user.setUsername(name);
		user.setPassword(pass);
		user.setEmail(email);
		return user;
	}
}