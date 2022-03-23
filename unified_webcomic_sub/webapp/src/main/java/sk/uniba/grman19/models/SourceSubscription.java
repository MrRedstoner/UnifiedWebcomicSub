package sk.uniba.grman19.models;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity(name = "source_sub")
public class SourceSubscription extends BaseEntity {
	/** generated */
	private static final long serialVersionUID = 121686848969752786L;

	@OneToOne
	@JoinColumn(name = "group_id")
	private SubGroup group;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "source_id")
	private Source source;

	public SubGroup getGroup() {
		return group;
	}

	public void setGroup(SubGroup group) {
		this.group = group;
	}

	public Source getSource() {
		return source;
	}

	public void setSource(Source source) {
		this.source = source;
	}
}
