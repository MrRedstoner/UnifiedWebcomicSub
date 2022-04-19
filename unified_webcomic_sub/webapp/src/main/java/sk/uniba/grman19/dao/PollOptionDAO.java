package sk.uniba.grman19.dao;

import sk.uniba.grman19.models.entity.PollOption;
import sk.uniba.grman19.models.entity.Post;

public interface PollOptionDAO {

	PollOption createOption(Post post, String option);

}
