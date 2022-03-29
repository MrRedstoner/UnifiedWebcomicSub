package sk.uniba.grman19.util;

import java.util.function.Function;

import sk.nociar.jpacloner.JpaCloner;
import sk.uniba.grman19.models.PaginatedList;

public class Cloner {
	public static <E> Function<E, E> clone(String... patterns) {
		return e -> JpaCloner.clone(e, patterns);
	}

	public static <E> Function<PaginatedList<E>, PaginatedList<E>> clonePaginated(String... patterns) {
		return pl -> new PaginatedList<>(pl.getTotal(), JpaCloner.clone(pl.getItems(), patterns));
	}
}
