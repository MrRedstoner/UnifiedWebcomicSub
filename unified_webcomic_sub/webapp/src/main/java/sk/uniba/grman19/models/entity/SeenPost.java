package sk.uniba.grman19.models.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity(name = "seen_post")
public class SeenPost extends BaseEntity {
	/** generated */
	private static final long serialVersionUID = -2996935390084387504L;

	@OneToOne
	@JoinColumn(name = "uid")
	private UWSUser user;
	@ManyToOne
	@JoinColumn(name = "post_id")
	private Post post;

	public SeenPost() {
	}

	public SeenPost(UWSUser user, Post post) {
		this.user = user;
		this.post = post;
	}

	public UWSUser getUser() {
		return user;
	}

	public void setUser(UWSUser user) {
		this.user = user;
	}

	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}
}
