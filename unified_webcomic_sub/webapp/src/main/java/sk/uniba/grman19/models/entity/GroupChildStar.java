package sk.uniba.grman19.models.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Immutable;

@Immutable
@Entity(name = "group_child_star")
@IdClass(GroupChildStarId.class)
public class GroupChildStar {
	@Id
	@OneToOne
	@JoinColumn(name = "parent")
	private SubGroup parent;
	@Id
	@OneToOne
	@JoinColumn(name = "child")
	private SubGroup child;

	public SubGroup getParent() {
		return parent;
	}

	public SubGroup getChild() {
		return child;
	}

	/*
	CREATE VIEW IF NOT EXISTS group_child_star AS
		WITH RECURSIVE GCS(P,C) AS (
				SELECT ID AS P, ID AS C FROM sub_group
			UNION ALL
				SELECT GCS.P AS P, GROUP_CHILD.CHILD_ID AS C FROM GCS JOIN GROUP_CHILD ON GCS.C=GROUP_CHILD.PARENT_ID
		)
	  	SELECT DISTINCT GCS.P AS parent, GCS.C as child FROM GCS;
	*/
}
