package sk.uniba.grman19.models.rest;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class SubGroupUpdate {
	@NotNull
	private Long id;
	@Pattern(regexp = "^(?!\\s*$).+", message = "Name must not be empty")
	private String name;
	@Pattern(regexp = "^(?!\\s*$).+", message = "Description must not be empty")
	private String description;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
}
