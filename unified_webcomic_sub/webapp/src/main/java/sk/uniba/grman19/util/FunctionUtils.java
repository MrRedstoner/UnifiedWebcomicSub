package sk.uniba.grman19.util;

import java.util.function.Function;

import sk.uniba.grman19.util.query.TriFunction;

public class FunctionUtils {
	public static final <A, B, C> Function<A, C> compose(Function<A, B> aToB, Function<B, C> bToC) {
		return bToC.compose(aToB);
	}

	public static final <A, B, C, D, E> TriFunction<B, C, A, E> mapLast(Function<A, D> aToD, TriFunction<B, C, D, E> bcdToE) {
		return (b, c, a) -> bcdToE.apply(b, c, aToD.apply(a));
	}
}
