package sk.uniba.grman19.models.entity;

import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
public class Source extends BaseEntity {
	/** generated */
	private static final long serialVersionUID = -456290309097991577L;

	private String name;
	private String description;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "source")
	private List<SourceAttribute> sourceAttribute;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "source")
	private List<SourceUpdate> sourceUpdates;
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "source")
	private List<SourceSubscription> sourceSubs;
	@Transient
	@JsonInclude(Include.NON_NULL)
	private Boolean subscribed;
	@Transient
	@JsonInclude(Include.NON_NULL)
	private Boolean ignored;

	public Source() {
	}

	public Source(String name, String desc) {
		this.name = name;
		this.description = desc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<SourceAttribute> getSourceAttribute() {
		return sourceAttribute;
	}

	public void setSourceAttribute(List<SourceAttribute> sourceAttribute) {
		this.sourceAttribute = sourceAttribute;
	}

	public List<SourceUpdate> getSourceUpdates() {
		return sourceUpdates;
	}

	public void setSourceUpdates(List<SourceUpdate> sourceUpdates) {
		this.sourceUpdates = sourceUpdates;
	}

	public List<SourceSubscription> getSourceSubs() {
		return sourceSubs;
	}

	public void setSourceSubs(List<SourceSubscription> sourceSubs) {
		this.sourceSubs = sourceSubs;
	}

	public Boolean getSubscribed() {
		return subscribed;
	}

	public void setSubscribed(Boolean subscribed) {
		this.subscribed = subscribed;
	}

	public Boolean getIgnored() {
		return ignored;
	}

	public void setIgnored(Boolean ignored) {
		this.ignored = ignored;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Source s = (Source) o;
		return Objects.equals(id, s.id) && Objects.equals(name, s.name) && Objects.equals(description, s.description);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, description);
	}

	@Override
	public String toString() {
		return "Source{" + "id=" + id + ", name='" + name + '\'' + ", description='" + description + '\'' + '}';
	}
}
