package sk.uniba.grman19.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sk.uniba.grman19.dao.AuditLogDAO;
import sk.uniba.grman19.dao.PollOptionDAO;
import sk.uniba.grman19.dao.PollVoteDAO;
import sk.uniba.grman19.dao.PostDAO;
import sk.uniba.grman19.filter.FilterColumn;
import sk.uniba.grman19.models.PaginatedList;
import sk.uniba.grman19.models.entity.PollOption;
import sk.uniba.grman19.models.entity.PollVote;
import sk.uniba.grman19.models.entity.Post;
import sk.uniba.grman19.models.entity.UWSUser;
import sk.uniba.grman19.util.ConversionUtils;
import sk.uniba.grman19.util.NotFoundException;

@Service
public class PostService {
	@Autowired
	private PostDAO postDao;
	@Autowired
	private PollOptionDAO pollOptionDao;
	@Autowired
	private PollVoteDAO pollVoteDao;
	@Autowired
	private AuditLogDAO auditLogDao;

	@Transactional(readOnly = false)
	public Post createPost(UWSUser user, String title, String content, List<String> options) {
		Date created = new Date();
		Post post = postDao.createPost(user, title, content, created);
		for (String option : options) {
			pollOptionDao.createOption(post, option);
		}
		auditLogDao.saveLog(user, "Posted " + post.getId());
		return post;
	}

	@Transactional(readOnly = true)
	public Optional<Post> getPost(Long id) {
		return postDao.getPost(id);
	}

	@Transactional(readOnly = true)
	public Post getPostWithVoteCounts(Long id) {
		Post result = getPost(id).orElseThrow(NotFoundException::new);
		Map<Long, Long> voteCounts = pollVoteDao.getVoteCounts(ConversionUtils.getIds(result.getOptions()));
		result.getOptions().forEach(opt -> {
			opt.setVoteCount(voteCounts.getOrDefault(opt.getId(), 0l));
		});
		return result;
	}

	@Transactional(readOnly = true)
	public PaginatedList<Post> getPosts(Integer offset, Integer limit, Map<FilterColumn, String> filters) {
		long count = postDao.getPostCount(filters);
		List<Post> items = postDao.getPosts(offset, limit, filters);
		return new PaginatedList<>(count, items);
	}

	@Transactional(readOnly = false)
	public PollVote setVote(UWSUser user, Long optionId) {
		PollOption option = pollOptionDao.getOption(optionId).orElseThrow(NotFoundException::new);
		auditLogDao.saveLog(user, "Voted " + optionId);
		return pollVoteDao.setUserVote(user, option);
	}
}
