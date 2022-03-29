package sk.uniba.grman19.models;

import java.io.Serializable;
import java.util.List;

public class PaginatedList<T> implements Serializable {
	/** generated */
	private static final long serialVersionUID = -4189123451969208760L;

	private long total;
	private List<T> items;

	public PaginatedList() {
	}

	public PaginatedList(long total, List<T> items) {
		this.total = total;
		this.items = items;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public List<T> getItems() {
		return items;
	}

	public void setItems(List<T> items) {
		this.items = items;
	}
}
