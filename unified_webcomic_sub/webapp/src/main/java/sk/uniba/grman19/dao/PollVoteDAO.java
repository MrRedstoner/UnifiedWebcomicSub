package sk.uniba.grman19.dao;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import sk.uniba.grman19.models.entity.PollOption;
import sk.uniba.grman19.models.entity.PollVote;
import sk.uniba.grman19.models.entity.Post;
import sk.uniba.grman19.models.entity.UWSUser;

public interface PollVoteDAO {
	Optional<PollVote> getUserVote(UWSUser user, Post post);

	PollVote setUserVote(UWSUser user, PollOption option);

	Map<Long, Long> getVoteCounts(Set<Long> options);
}
