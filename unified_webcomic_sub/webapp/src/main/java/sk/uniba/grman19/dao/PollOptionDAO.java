package sk.uniba.grman19.dao;

import java.util.Optional;

import sk.uniba.grman19.models.entity.PollOption;
import sk.uniba.grman19.models.entity.Post;

public interface PollOptionDAO {
	Optional<PollOption> getOption(Long id);

	PollOption createOption(Post post, String option);

}
