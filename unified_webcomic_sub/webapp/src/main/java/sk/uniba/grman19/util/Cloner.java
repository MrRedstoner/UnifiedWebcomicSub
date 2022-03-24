package sk.uniba.grman19.util;

import java.util.function.Function;

import sk.nociar.jpacloner.JpaCloner;

public class Cloner {
	public static <E> Function<E, E> clone(String... patterns) {
		return e -> JpaCloner.clone(e, patterns);
	}
}
