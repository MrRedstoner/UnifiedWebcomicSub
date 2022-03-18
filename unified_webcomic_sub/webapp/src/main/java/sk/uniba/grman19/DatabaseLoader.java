package sk.uniba.grman19;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import sk.uniba.grman19.models.Source;
import sk.uniba.grman19.repository.MSRepository;
import sk.uniba.grman19.repository.SourceRepository;

@Component
public class DatabaseLoader implements CommandLineRunner {

	private final SourceRepository repository;
	@Autowired
	private MSRepository mSRepository;

	@Autowired
	public DatabaseLoader(SourceRepository repository) {
		this.repository = repository;
	}

	// some test data
	@Override
	public void run(String... strings) throws Exception {
		// repository.save(new Source("s1", "des1"));
		// repository.findAll().forEach(System.out::println);
		System.out.println("Dumping MS");
		mSRepository.findAll().forEach(System.out::println);
		System.out.println("Dumping MS done");
	}
}