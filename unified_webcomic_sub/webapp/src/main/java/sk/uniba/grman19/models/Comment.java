package sk.uniba.grman19.models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "post_comment")
public class Comment extends BaseEntity {
	/** generated */
	private static final long serialVersionUID = -956075989330333981L;

	@ManyToOne
	@JoinColumn(name = "uid")
	private UWSUser user;
	@ManyToOne
	@JoinColumn(name = "post_id")
	private Post post;
	private String content;
	private Date created;

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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
}
