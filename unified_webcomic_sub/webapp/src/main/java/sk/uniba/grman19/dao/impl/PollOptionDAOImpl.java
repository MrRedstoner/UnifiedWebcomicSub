package sk.uniba.grman19.dao.impl;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import sk.uniba.grman19.dao.PollOptionDAO;
import sk.uniba.grman19.models.entity.PollOption;
import sk.uniba.grman19.models.entity.Post;

@Component
@Transactional(readOnly = true)
public class PollOptionDAOImpl implements PollOptionDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@Transactional(readOnly = false)
	public PollOption createOption(Post post, String option) {
		PollOption pollOption = new PollOption(post, option);
		entityManager.persist(pollOption);
		return pollOption;
	}

	@Override
	public Optional<PollOption> getOption(Long id) {
		return Optional.ofNullable(entityManager.find(PollOption.class, id));
	}

}
