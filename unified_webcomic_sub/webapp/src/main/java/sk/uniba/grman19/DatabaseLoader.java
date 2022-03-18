package sk.uniba.grman19;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import sk.uniba.grman19.models.Source;
import sk.uniba.grman19.models.UWSUser;
import sk.uniba.grman19.repository.MSRepository;
import sk.uniba.grman19.repository.SourceRepository;
import sk.uniba.grman19.repository.UWSUserRepository;

@Component
public class DatabaseLoader implements CommandLineRunner {

	private final SourceRepository repository;
	@Autowired
	private MSRepository mSRepository;
	@Autowired
	private UWSUserRepository userRepository;

	@Autowired
	public DatabaseLoader(SourceRepository repository) {
		this.repository = repository;
	}

	// some test data
	@Override
	public void run(String... strings) throws Exception {
		repository.save(new Source("s1", "des1"));
		repository.save(new Source("s2", "des2"));
		repository.save(new Source("s3", "des3"));
		//repository.findAll().forEach(System.out::println);
		userRepository.save(new UWSUser("root", "usbw", true, false, false, false, false, false));
		userRepository.save(new UWSUser("user", "user", false, false, false, false, false, false));
		/*System.out.println("Dumping MS");
		mSRepository.findAll().forEach(System.out::println);
		System.out.println("Dumping MS done");*/
	}
}