package sk.uniba.grman19.models.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity(name = "post_sub")
public class PostSubscription extends BaseEntity {
	/** generated */
	private static final long serialVersionUID = 2336111777112831084L;

	@OneToOne
	@JoinColumn(name = "group_id")
	private SubGroup group;
	@OneToOne
	@JoinColumn(name = "uid")
	private UWSUser user;

	public PostSubscription() {
	}

	public PostSubscription(SubGroup group, UWSUser user) {
		this.group = group;
		this.user = user;
	}

	public SubGroup getGroup() {
		return group;
	}

	public void setGroup(SubGroup group) {
		this.group = group;
	}

	public UWSUser getUser() {
		return user;
	}

	public void setUser(UWSUser user) {
		this.user = user;
	}
}
