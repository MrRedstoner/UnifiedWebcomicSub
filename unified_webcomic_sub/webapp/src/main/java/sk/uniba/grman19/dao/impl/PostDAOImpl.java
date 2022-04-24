package sk.uniba.grman19.dao.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import sk.uniba.grman19.dao.PostDAO;
import sk.uniba.grman19.filter.FilterColumn;
import sk.uniba.grman19.filter.FilterMapper;
import sk.uniba.grman19.models.entity.Post;
import sk.uniba.grman19.models.entity.Post_;
import sk.uniba.grman19.models.entity.UWSUser;
import sk.uniba.grman19.util.query.FilterMapperQuery;

@Component
@Transactional(readOnly = true)
public class PostDAOImpl implements PostDAO {
	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	@Qualifier("postQueryByFilter")
	private FilterMapperQuery<Post> queryByFilter;

	@Override
	public Optional<Post> getPost(Long id) {
		return Optional.ofNullable(entityManager.find(Post.class, id));
	}

	@Override
	@Transactional(readOnly = false)
	public Post createPost(UWSUser user, String title, String content, Date created) {
		Post post = new Post(user, title, content, created);
		entityManager.persist(post);
		return post;
	}

	@Override
	public long getPostCount(Map<FilterColumn, String> filters) {
		return queryByFilter.queryCount(filters);
	}

	@Override
	public List<Post> getPosts(Integer offset, Integer limit, Map<FilterColumn, String> filters) {
		return queryByFilter.queryList(offset, limit, filters);
	}

	private static FilterMapper makeFilterMapper(CriteriaBuilder cb, Root<Post> root) {
		return new FilterMapper(cb)
			.addNumberFilter(FilterColumn.ID, root.get(Post_.id))
			.addStringFilter(FilterColumn.TITLE, root.get(Post_.title));
	}

	@Configuration
	static class Config {
		@Bean(name = "postQueryByFilter")
		@PersistenceContext
		FilterMapperQuery<Post> queryByFilter(EntityManager entityManager) {
			return new FilterMapperQuery<>(entityManager, Post.class, PostDAOImpl::makeFilterMapper);
		}
	}
}
