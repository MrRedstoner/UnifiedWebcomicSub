package sk.uniba.grman19.models.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity(name = "mail_setting")
public class MailSettings extends BaseEntity {
	/** generated */
	private static final long serialVersionUID = -504521804069644070L;

	@OneToOne
	@JoinColumn(name = "uid")
	private UWSUser user;
	@Column(name = "mail_addr")
	private String mailAddress;
	@OneToOne
	@JoinColumn(name = "subscribe")
	private SubGroup subscribe;
	@OneToOne
	@JoinColumn(name = "ignore")
	private SubGroup ignore;
	private Boolean daily;
	private Boolean weekly;
	@Column(name = "day_of_week")
	private Byte dayOfWeek;
	@Column(name = "last_daily")
	private Date lastDaily;
	@Column(name = "last_weekly")
	private Date lastWeekly;

	public MailSettings() {
	}

	public MailSettings(UWSUser user, String mailAddress, SubGroup subscribe, SubGroup ignore, boolean daily, boolean weekly, byte dayOfWeek, Date lastDaily, Date lastWeekly) {
		this.user = user;
		this.mailAddress = mailAddress;
		this.subscribe = subscribe;
		this.ignore = ignore;
		this.daily = daily;
		this.weekly = weekly;
		this.dayOfWeek = dayOfWeek;
		this.lastDaily = lastDaily;
		this.lastWeekly = lastWeekly;
	}

	public UWSUser getUser() {
		return user;
	}

	public void setUser(UWSUser user) {
		this.user = user;
	}

	public String getMailAddress() {
		return mailAddress;
	}

	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}

	public SubGroup getSubscribe() {
		return subscribe;
	}

	public void setSubscribe(SubGroup subscribe) {
		this.subscribe = subscribe;
	}

	public SubGroup getIgnore() {
		return ignore;
	}

	public void setIgnore(SubGroup ignore) {
		this.ignore = ignore;
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
