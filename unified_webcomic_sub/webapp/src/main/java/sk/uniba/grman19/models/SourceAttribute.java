package sk.uniba.grman19.models;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "source_attr")
public class SourceAttribute extends BaseEntity {
	/** generated */
	private static final long serialVersionUID = -5760860350216170623L;

	private String name;
	private String value;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "source_id")
	private Source source;

	public SourceAttribute() {
	}

	public SourceAttribute(Source source, String name, String value) {
		this.source = source;
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Source getSource() {
		return source;
	}

	public void setSource(Source source) {
		this.source = source;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		SourceAttribute sa = (SourceAttribute) o;
		return Objects.equals(id, sa.id) && Objects.equals(name, sa.name) && Objects.equals(value, sa.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, value);
	}

	@Override
	public String toString() {
		return "SourceAttribute{" + "id=" + id + ", name='" + name + '\'' + ", value='" + value + '\'' + '}';
	}
}
