package sk.uniba.grman19.models.entity;

import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name = "uws_user")
public class UWSUser extends BaseEntity {
	/** generated */
	private static final long serialVersionUID = 3755946416573106039L;

	private String name;
	@JsonIgnore
	private String password;
	private Boolean owner;
	private Boolean admin;
	@Column(name = "create_post")
	private Boolean createPost;
	@Column(name = "create_source")
	private Boolean createSource;
	@Column(name = "edit_source")
	private Boolean editSource;
	@Column(name = "edit_group")
	private Boolean editGroup;

	@OneToOne(mappedBy = "user")
	private MailSettings mailSettings;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	private List<AuditLog> auditLog;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	private List<Post> posts;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	private Set<PollVote> votes;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	private Set<SeenUpdate> seenUpdates;

	public UWSUser() {
	}

	public UWSUser(String name, String password) {
		this(name, password, false, false, false, false, false, false);
	}

	public UWSUser(String name, String password, boolean owner, boolean admin, boolean createPost, boolean createSource, boolean editSource, boolean editGroup) {
		this.name = name;
		this.setPassword(password);
		this.owner = owner;
		this.admin = admin;
		this.createPost = createPost;
		this.createSource = createSource;
		this.editSource = editSource;
		this.editGroup = editGroup;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getOwner() {
		return owner;
	}

	public void setOwner(Boolean owner) {
		this.owner = owner;
	}

	public Boolean getAdmin() {
		return admin;
	}

	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}

	public Boolean getCreatePost() {
		return createPost;
	}

	public void setCreatePost(Boolean createPost) {
		this.createPost = createPost;
	}

	public Boolean getCreateSource() {
		return createSource;
	}

	public void setCreateSource(Boolean createSource) {
		this.createSource = createSource;
	}

	public Boolean getEditSource() {
		return editSource;
	}

	public void setEditSource(Boolean editSource) {
		this.editSource = editSource;
	}

	public Boolean getEditGroup() {
		return editGroup;
	}

	public void setEditGroup(Boolean editGroup) {
		this.editGroup = editGroup;
	}

	public MailSettings getMailSettings() {
		return mailSettings;
	}

	public void setMailSettings(MailSettings mailSettings) {
		this.mailSettings = mailSettings;
	}

	public List<AuditLog> getAuditLog() {
		return auditLog;
	}

	public void setAuditLog(List<AuditLog> auditLog) {
		this.auditLog = auditLog;
	}

	public List<Post> getPosts() {
		return posts;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}

	public Set<PollVote> getVotes() {
		return votes;
	}

	public void setVotes(Set<PollVote> votes) {
		this.votes = votes;
	}

	public Set<SeenUpdate> getSeenUpdates() {
		return seenUpdates;
	}

	public void setSeenUpdates(Set<SeenUpdate> seenUpdates) {
		this.seenUpdates = seenUpdates;
	}
}
