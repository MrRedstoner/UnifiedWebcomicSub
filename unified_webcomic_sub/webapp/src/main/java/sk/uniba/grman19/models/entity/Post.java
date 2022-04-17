package sk.uniba.grman19.models.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Post extends BaseEntity {
	/** generated */
	private static final long serialVersionUID = 1725610844674299960L;

	@ManyToOne
	@JoinColumn(name = "uid")
	private UWSUser user;
	private String title;
	private String content;
	private Date created;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "post")
	private List<Comment> comments;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "post")
	private List<PollOption> options;

	public Post() {
	}

	public Post(UWSUser user, String title, String content, Date created) {
		this.user = user;
		this.title = title;
		this.content = content;
		this.created = created;
	}

	public UWSUser getUser() {
		return user;
	}

	public void setUser(UWSUser user) {
		this.user = user;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public List<PollOption> getOptions() {
		return options;
	}

	public void setOptions(List<PollOption> options) {
		this.options = options;
	}
}
