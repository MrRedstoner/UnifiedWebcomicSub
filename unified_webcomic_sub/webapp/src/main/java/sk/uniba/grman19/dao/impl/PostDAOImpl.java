package sk.uniba.grman19.dao.impl;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import sk.uniba.grman19.dao.PostDAO;
import sk.uniba.grman19.models.entity.Post;
import sk.uniba.grman19.models.entity.UWSUser;

@Component
@Transactional(readOnly = true)
public class PostDAOImpl implements PostDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@Transactional(readOnly = false)
	public Post createPost(UWSUser user, String title, String content, Date created) {
		Post post = new Post(user, title, content, created);
		entityManager.persist(post);
		return post;
	}

}
