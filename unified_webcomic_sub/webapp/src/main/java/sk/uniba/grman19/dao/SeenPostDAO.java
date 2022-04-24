package sk.uniba.grman19.dao;

import sk.uniba.grman19.models.entity.Post;
import sk.uniba.grman19.models.entity.SeenPost;
import sk.uniba.grman19.models.entity.UWSUser;

public interface SeenPostDAO {
	SeenPost updateSeenPost(UWSUser user, Post post);
}
