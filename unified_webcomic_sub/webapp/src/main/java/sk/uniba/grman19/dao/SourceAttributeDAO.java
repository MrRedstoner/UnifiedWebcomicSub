package sk.uniba.grman19.dao;

import java.util.Map;
import java.util.Set;

import sk.uniba.grman19.models.entity.Source;
import sk.uniba.grman19.models.entity.SourceAttribute;

public interface SourceAttributeDAO {
	void updateAttributes(Source source, Map<String, String> create, Map<String, String> update, Set<String> delete);

	SourceAttribute addAttribute(Source source, String name, String value);

	SourceAttribute updateAttribute(Source source, String name, String value);

	void deleteAttribute(Source source, String name);
}
