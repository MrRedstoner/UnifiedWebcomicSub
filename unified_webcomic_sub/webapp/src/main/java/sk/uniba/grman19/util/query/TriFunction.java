package sk.uniba.grman19.util.query;

import java.util.function.BiFunction;
import java.util.function.Function;

@FunctionalInterface
public interface TriFunction<A0, A1, A2, R> {
	public R apply(A0 a, A1 b, A2 c);

	public static <A0, A1, A2, R> TriFunction<A0, A1, A2, R> asTri(BiFunction<A0, A1, R> fun) {
		return (a0, a1, a2) -> fun.apply(a0, a1);
	}

	public default <A> TriFunction<A, A1, A2, R> compose(Function<A, A0> before) {
		return (a, a0, a1) -> apply(before.apply(a), a0, a1);
	}
}
