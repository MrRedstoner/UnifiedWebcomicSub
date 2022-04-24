package sk.uniba.grman19.models.entity;

import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity(name = "sub_group")
public class SubGroup extends BaseEntity {
	/** generated */
	private static final long serialVersionUID = 3906165369467440761L;

	private String name;
	private String description;
	@Column(name = "user_owned")
	private Boolean userOwned;

	@JsonProperty("sources")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "group")
	private List<SourceSubscription> sourceSubs;
	@JsonProperty("posters")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "group")
	private List<PostSubscription> postSubs;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "child")
	private List<GroupChild> parents;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
	private List<GroupChild> children;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
	private Set<GroupChildStar> childrenStar;
	@Transient
	@JsonInclude(Include.NON_NULL)
	private Boolean subscribed;

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

	public List<GroupChild> getParents() {
		return parents;
	}

	public void setParents(List<GroupChild> parents) {
		this.parents = parents;
	}

	public List<GroupChild> getChildren() {
		return children;
	}

	public void setChildren(List<GroupChild> children) {
		this.children = children;
	}

	public Set<GroupChildStar> getChildrenStar() {
		return childrenStar;
	}

	public void setChildrenStar(Set<GroupChildStar> childrenStar) {
		this.childrenStar = childrenStar;
	}

	public Boolean getSubscribed() {
		return subscribed;
	}

	public void setSubscribed(Boolean subscribed) {
		this.subscribed = subscribed;
	}
}
