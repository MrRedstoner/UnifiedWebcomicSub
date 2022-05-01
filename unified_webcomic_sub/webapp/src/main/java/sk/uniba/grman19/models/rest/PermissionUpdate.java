package sk.uniba.grman19.models.rest;

import javax.validation.constraints.NotNull;

public class PermissionUpdate {
	@NotNull
	private Long id;
	private Boolean admin;
	private Boolean createPost;
	private Boolean createSource;
	private Boolean editSource;
	private Boolean editGroup;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
}
