package sk.uniba.grman19.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "audit_log")
public class AuditLog extends BaseEntity {
	/** generated */
	private static final long serialVersionUID = 1486856689204353024L;

	@ManyToOne
	@JoinColumn(name = "uid")
	private UWSUser user;
	@Column(name = "change_time")
	private Date changeTime;
	private String description;

	public AuditLog() {
	}

	public AuditLog(UWSUser user, Date changeTime, String description) {
		this.user = user;
		this.changeTime = changeTime;
		this.description = description;
	}

	public UWSUser getUser() {
		return user;
	}

	public void setUser(UWSUser user) {
		this.user = user;
	}

	public Date getChangeTime() {
		return changeTime;
	}

	public void setChangeTime(Date changeTime) {
		this.changeTime = changeTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
