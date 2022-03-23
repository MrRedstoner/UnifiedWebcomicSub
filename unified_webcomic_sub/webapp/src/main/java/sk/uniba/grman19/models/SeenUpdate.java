package sk.uniba.grman19.models;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity(name = "seen_update")
public class SeenUpdate extends BaseEntity {
	/** generated */
	private static final long serialVersionUID = 1093325255011080830L;

	@OneToOne
	@JoinColumn(name = "uid")
	private UWSUser user;
	@ManyToOne
	@JoinColumn(name = "update_id")
	private SourceUpdate update;

	public UWSUser getUser() {
		return user;
	}

	public void setUser(UWSUser user) {
		this.user = user;
	}

	public SourceUpdate getUpdate() {
		return update;
	}

	public void setUpdate(SourceUpdate update) {
		this.update = update;
	}
}
