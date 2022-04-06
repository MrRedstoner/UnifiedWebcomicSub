package sk.uniba.grman19.util;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import sk.uniba.grman19.models.entity.SourceAttribute;

public class ConversionUtils {
	public static Map<String, String> toMap(Collection<SourceAttribute> attributes) {
		return attributes.stream()
			.collect(Collectors.toMap(SourceAttribute::getName, SourceAttribute::getValue));
	}
}
