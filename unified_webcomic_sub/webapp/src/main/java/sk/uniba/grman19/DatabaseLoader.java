package sk.uniba.grman19;

import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import sk.uniba.grman19.models.entity.Source;
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
		IntStream.range(0, 20).forEach(this::createSource);
		UserRegistration user;
		UWSUser uuser;
		user = makeUserRegistration("root", "usbw", "root@root");
		uuser = userService.registerUser(user);
		uuser.setOwner(true);
		userRepository.save(uuser);
		user = makeUserRegistration("user", "user", "user@root");
		uuser = userService.registerUser(user);
	}

	private void createSource(int number) {
		sourceRepository.save(new Source("source" + number, "descr" + number));
	}

	private UserRegistration makeUserRegistration(String name, String pass, String email) {
		UserRegistration user = new UserRegistration();
		user.setUsername(name);
		user.setPassword(pass);
		user.setEmail(email);
		return user;
	}
}