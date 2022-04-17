package sk.uniba.grman19.dao;

import java.util.Date;

import sk.uniba.grman19.models.entity.Post;
import sk.uniba.grman19.models.entity.UWSUser;

public interface PostDAO {
	Post createPost(UWSUser user, String title, String content, Date created);
}
