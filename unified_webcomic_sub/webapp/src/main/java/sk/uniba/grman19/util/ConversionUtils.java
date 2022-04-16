package sk.uniba.grman19.util;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import sk.uniba.grman19.models.entity.BaseEntity;
import sk.uniba.grman19.models.entity.SourceAttribute;

public class ConversionUtils {
	public static Date toUtilDate(LocalDate date) {
	    return java.sql.Date.valueOf(date);
	}

	public static Map<String, String> toMap(Collection<SourceAttribute> attributes) {
		return attributes.stream()
			.collect(Collectors.toMap(SourceAttribute::getName, SourceAttribute::getValue));
	}

	public static Set<Long> getIds(Collection<? extends BaseEntity> entities) {
		return entities.stream()
			.map(BaseEntity::getId)
			.collect(Collectors.toSet());
	}
}
