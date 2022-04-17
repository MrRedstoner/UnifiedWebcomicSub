package sk.uniba.grman19.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sk.uniba.grman19.dao.AuditLogDAO;
import sk.uniba.grman19.dao.PostDAO;
import sk.uniba.grman19.models.entity.Post;
import sk.uniba.grman19.models.entity.UWSUser;

@Service
public class PostService {
	@Autowired
	private PostDAO postDao;
	@Autowired
	private AuditLogDAO auditLogDao;

	@Transactional(readOnly = false)
	public Post createPost(UWSUser user, String title, String content) {
		Date created = new Date();
		Post post = postDao.createPost(user, title, content, created);
		auditLogDao.saveLog(user, "Posted " + post.getId());
		return post;
	}

}
