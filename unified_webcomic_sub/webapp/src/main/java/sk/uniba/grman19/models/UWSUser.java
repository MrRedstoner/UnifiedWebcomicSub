package sk.uniba.grman19.models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name = "uws_user")
public class UWSUser extends BaseEntity {
	/** generated */
	private static final long serialVersionUID = 3755946416573106039L;
	public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

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

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id", referencedColumnName = "uid")
	private MailSettings mailSettings;

	public UWSUser() {
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
		this.password = PASSWORD_ENCODER.encode(password);
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
	// TODO equals hashCode toString
}
