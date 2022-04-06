package sk.uniba.grman19.models.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "source_update")
public class SourceUpdate extends BaseEntity {
	/** generated */
	private static final long serialVersionUID = -782736394199655089L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "source_id")
	private Source source;
	@Column(name = "update_time")
	private Date updateTime;
	private String value;

	public SourceUpdate() {
	}

	public SourceUpdate(Source source, String value, Date updateTime) {
		this.source = source;
		this.value = value;
		this.updateTime = updateTime;
	}

	public Source getSource() {
		return source;
	}

	public void setSource(Source source) {
		this.source = source;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
