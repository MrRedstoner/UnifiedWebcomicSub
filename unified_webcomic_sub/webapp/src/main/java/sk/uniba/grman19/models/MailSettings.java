package sk.uniba.grman19.models;

import java.util.Date;

import javax.persistence.Entity;

@Entity(name = "mail_setting")
public class MailSettings extends BaseEntity {
	/** generated */
	private static final long serialVersionUID = -504521804069644070L;
	// TODO mapped UWSUser
	private String mailAddress;
	// mapped sub group
	// mapped ignore group
	private Boolean daily;
	private Boolean weekly;
	private Byte dayOfWeek;
	private Date lastDaily;
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
}
