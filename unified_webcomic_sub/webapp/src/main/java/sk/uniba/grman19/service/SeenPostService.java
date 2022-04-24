package sk.uniba.grman19.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sk.uniba.grman19.dao.SeenPostDAO;
import sk.uniba.grman19.models.entity.Post;
import sk.uniba.grman19.models.entity.UWSUser;

@Service
public class SeenPostService {
	@Autowired
	private SeenPostDAO seenPostDao;

	@Transactional(readOnly = false)
	public void updateSeenPosts(UWSUser user, List<Post> updates) {
		Map<Long, Post> newest = new HashMap<>();
		for (Post update : updates) {
			Long source = update.getUser().getId();
			if (newest.containsKey(source)) {
				newest.compute(source, (s, old) -> getNewer(old, update));
			} else {
				newest.put(source, update);
			}
		}

		for (Post update : newest.values()) {
			seenPostDao.updateSeenPost(user, update);
		}
	}

	private Post getNewer(Post update0, Post update1) {
		if (update1.getCreated().after(update0.getCreated())) {
			return update1;
		} else {
			return update0;
		}
	}
}
