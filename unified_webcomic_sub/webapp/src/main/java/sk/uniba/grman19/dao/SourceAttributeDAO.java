package sk.uniba.grman19.dao;

import sk.uniba.grman19.models.entity.Source;
import sk.uniba.grman19.models.entity.SourceAttribute;

public interface SourceAttributeDAO {
	public SourceAttribute addAttribute(Source source, String key, String value);
}
