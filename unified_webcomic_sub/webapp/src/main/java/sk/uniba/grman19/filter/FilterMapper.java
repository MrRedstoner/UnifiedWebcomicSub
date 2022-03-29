package sk.uniba.grman19.filter;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;

import sk.uniba.grman19.util.BadRequestException;

public class FilterMapper {
	private final CriteriaBuilder cb;
	private final EnumMap<FilterColumn, Function<String, Predicate>> colFilters = new EnumMap<>(FilterColumn.class);

	public FilterMapper(CriteriaBuilder cb) {
		this.cb = cb;
	}

	public FilterMapper addBooleanFilter(FilterColumn column, Expression<Boolean> atom) {
		colFilters.put(column, s -> booleanFilter(s, atom));
		return this;
	}

	public FilterMapper addNumberFilter(FilterColumn column, Expression<? extends Number> atom) {
		colFilters.put(column, s -> numberFilter(s, atom));
		return this;
	}

	public FilterMapper addStringFilter(FilterColumn column, Expression<String> atom) {
		colFilters.put(column, s -> stringFilter(s, atom));
		return this;
	}

	public Predicate[] processFilters(Map<FilterColumn, String> filters) {
		return filters.entrySet()
			.stream()
			.filter(e -> colFilters.containsKey(e.getKey()))
			.map(e -> colFilters.get(e.getKey()).apply(e.getValue()))
			.toArray(Predicate[]::new);
	}

	private Predicate stringFilter(String filter, Expression<String> atom) {
		if (filter.startsWith("\"") && filter.endsWith("\"")) {
			return cb.equal(atom, cb.literal(filter.substring(1, filter.length() - 1)));
		}
		// TODO something nicer, like glob-like matching on the front-end
		return cb.like(atom, filter);
	}

	private Predicate numberFilter(String filter, Expression<? extends Number> atom) {
		Long number;
		try {
			number = Long.parseLong(filter);
		} catch (NumberFormatException e) {
			throw new BadRequestException("Incorrect number filter");
		}
		return cb.equal(atom, cb.literal(number));
	}

	private Predicate booleanFilter(String filter, Expression<Boolean> atom) {
		if (filter.equals("1")) {
			return cb.isTrue(atom);
		} else {
			return cb.isFalse(atom);
		}
	}
}
