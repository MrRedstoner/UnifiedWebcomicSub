package sk.uniba.grman19.models.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity(name = "poll_vote")
public class PollVote extends BaseEntity {
	/** generated */
	private static final long serialVersionUID = 6454392715672811643L;

	@OneToOne
	@JoinColumn(name = "uid")
	private UWSUser user;
	@ManyToOne
	@JoinColumn(name = "option_id")
	private PollOption option;

	public UWSUser getUser() {
		return user;
	}

	public void setUser(UWSUser user) {
		this.user = user;
	}

	public PollOption getOption() {
		return option;
	}

	public void setOption(PollOption option) {
		this.option = option;
	}
}
