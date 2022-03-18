package sk.uniba.grman19.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity(name = "mail_setting")
public class MailSettings extends BaseEntity {
	/** generated */
	private static final long serialVersionUID = -504521804069644070L;

	@OneToOne(mappedBy = "mailSettings")
	private UWSUser user;
	@Column(name = "mail_addr")
	private String mailAddress;
	// TODO mapped sub group
	// TODO mapped ignore group
	private Boolean daily;
	private Boolean weekly;
	@Column(name = "day_of_week")
	private Byte dayOfWeek;
	@Column(name = "last_daily")
	private Date lastDaily;
	@Column(name = "last_weekly")
	private Date lastWeekly;

	public String getMailAddress() {
		return mailAddress;
	}

	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}

	public Boolean getDaily() {
		return daily;
	}

	public void setDaily(Boolean daily) {
		this.daily = daily;
	}

	public Boolean getWeekly() {
		return weekly;
	}

	public void setWeekly(Boolean weekly) {
		this.weekly = weekly;
	}

	public Byte getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(Byte dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public Date getLastDaily() {
		return lastDaily;
	}

	public void setLastDaily(Date lastDaily) {
		this.lastDaily = lastDaily;
	}

	public Date getLastWeekly() {
		return lastWeekly;
	}

	public void setLastWeekly(Date lastWeekly) {
		this.lastWeekly = lastWeekly;
	}

	// TODO equals hashCode toString
	public String toString() {
		return "MS: " + getId() + " user " + user.getName();
	}
}
