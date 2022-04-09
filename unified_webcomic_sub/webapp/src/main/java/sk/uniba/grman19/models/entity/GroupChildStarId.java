package sk.uniba.grman19.models.entity;

import java.io.Serializable;
import java.util.Objects;

public class GroupChildStarId implements Serializable {
	/** generated */
	private static final long serialVersionUID = -5869329503265573932L;
	private SubGroup parent;
	private SubGroup child;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		GroupChildStarId s = (GroupChildStarId) o;
		return Objects.equals(parent.getId(), s.parent.id) && Objects.equals(child.getId(), s.child.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(parent.getId(), child.getId());
	}
}
