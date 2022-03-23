package sk.uniba.grman19.models;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity(name = "poll_option")
public class PollOption extends BaseEntity {
	/** generated */
	private static final long serialVersionUID = 2995989357816505248L;

	@ManyToOne
	@JoinColumn(name = "post_id")
	private Post post;
	private String content;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "option")
	private Set<PollVote> votes;

	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Set<PollVote> getVotes() {
		return votes;
	}

	public void setVotes(Set<PollVote> votes) {
		this.votes = votes;
	}
}
