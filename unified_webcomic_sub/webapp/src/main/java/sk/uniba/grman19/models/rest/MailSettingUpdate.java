package sk.uniba.grman19.models.rest;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import sk.uniba.grman19.validation.ValidEmail;

public class MailSettingUpdate {
	@ValidEmail
	private String mailAddress;
	private Boolean daily;
	private Boolean weekly;
	@Min(value = 0, message = "Invalid day of week")
	@Max(value = 6, message = "Invalid day of week")
	private Byte dayOfWeek;

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
}
