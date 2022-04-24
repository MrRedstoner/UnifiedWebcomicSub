package sk.uniba.grman19.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import sk.uniba.grman19.filter.FilterColumn;
import sk.uniba.grman19.models.entity.Post;
import sk.uniba.grman19.models.entity.UWSUser;

public interface PostDAO {
	Optional<Post> getPost(Long id);

	Post createPost(UWSUser user, String title, String content, Date created);

	long getPostCount(Map<FilterColumn, String> filters);

	List<Post> getPosts(Integer offset, Integer limit, Map<FilterColumn, String> filters);

	List<Post> getUnseenPosts(Long userId, Set<Long> posterIds);
}
