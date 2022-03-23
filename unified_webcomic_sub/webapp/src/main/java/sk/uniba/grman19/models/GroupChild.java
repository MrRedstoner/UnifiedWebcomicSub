package sk.uniba.grman19.models;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity(name = "group_child")
public class GroupChild extends BaseEntity {
	/** generated */
	private static final long serialVersionUID = 7630950779030808903L;

	@OneToOne
	@JoinColumn(name = "parent_id")
	private SubGroup parent;
	@OneToOne
	@JoinColumn(name = "child_id")
	private SubGroup child;

	public SubGroup getParent() {
		return parent;
	}

	public void setParent(SubGroup parent) {
		this.parent = parent;
	}

	public SubGroup getChild() {
		return child;
	}

	public void setChild(SubGroup child) {
		this.child = child;
	}
}
