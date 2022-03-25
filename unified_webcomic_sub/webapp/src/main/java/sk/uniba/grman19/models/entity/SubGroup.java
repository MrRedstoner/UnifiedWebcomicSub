package sk.uniba.grman19.models.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Entity(name = "sub_group")
public class SubGroup extends BaseEntity {
	/** generated */
	private static final long serialVersionUID = 3906165369467440761L;

	private String name;
	private String description;
	@Column(name = "user_owned")
	private Boolean userOwned;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "group")
	private List<SourceSubscription> sourceSubs;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "group")
	private List<PostSubscription> postSubs;

	public SubGroup() {
	}

	public SubGroup(String name, String description, boolean userOwned) {
		this.name = name;
		this.description = description;
		this.userOwned = userOwned;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getUserOwned() {
		return userOwned;
	}

	public void setUserOwned(Boolean userOwned) {
		this.userOwned = userOwned;
	}

	public List<SourceSubscription> getSourceSubs() {
		return sourceSubs;
	}

	public void setSourceSubs(List<SourceSubscription> sourceSubs) {
		this.sourceSubs = sourceSubs;
	}

	public List<PostSubscription> getPostSubs() {
		return postSubs;
	}

	public void setPostSubs(List<PostSubscription> postSubs) {
		this.postSubs = postSubs;
	}
}
