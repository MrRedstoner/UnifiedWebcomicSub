package sk.uniba.grman19;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import sk.uniba.grman19.models.MailSettings;
import sk.uniba.grman19.models.Source;
import sk.uniba.grman19.models.UWSUser;
import sk.uniba.grman19.repository.MailSettingsRepository;
import sk.uniba.grman19.repository.SourceRepository;
import sk.uniba.grman19.repository.UWSUserRepository;

@Component
public class DatabaseLoader implements CommandLineRunner {

	@Autowired
	private SourceRepository sourceRepository;
	@Autowired
	private MailSettingsRepository mailSettingRepository;
	@Autowired
	private UWSUserRepository userRepository;

	// some test data
	@Override
	public void run(String... strings) throws Exception {
		sourceRepository.save(new Source("s1", "des1"));
		sourceRepository.save(new Source("s2", "des2"));
		sourceRepository.save(new Source("s3", "des3"));
		Long id;
		id=userRepository.save(new UWSUser("root", "usbw", true, false, false, false, false, false)).getId();
		mailSettingRepository.save(new MailSettings(id, "root@root", false, false, (byte)0, new Date(), new Date()));
		id=userRepository.save(new UWSUser("user", "user", false, false, false, false, false, false)).getId();
		mailSettingRepository.save(new MailSettings(id, "user@root", false, false, (byte)0, new Date(), new Date()));
	}
}