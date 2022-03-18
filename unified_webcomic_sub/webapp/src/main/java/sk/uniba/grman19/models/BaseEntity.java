package sk.uniba.grman19.models;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseEntity implements Serializable {
	/** generated */
	private static final long serialVersionUID = 2676067726347252821L;
	@Id
	@GeneratedValue
	protected Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	// TODO force inclusion without this
	public String getUseId() {
		return Objects.toString(id);
	}
}