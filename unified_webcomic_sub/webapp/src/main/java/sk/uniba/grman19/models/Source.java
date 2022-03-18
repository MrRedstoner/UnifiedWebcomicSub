package sk.uniba.grman19.models;

import java.util.Objects;

import javax.persistence.Entity;

@Entity
public class Source extends BaseEntity {
	/** generated */
	private static final long serialVersionUID = -456290309097991577L;

	private String name;
	private String description;

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

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Source employee = (Source) o;
		return Objects.equals(id, employee.id) && Objects.equals(name, employee.name)
				&& Objects.equals(description, employee.description);
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
