package sk.uniba.grman19.dao;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import sk.uniba.grman19.filter.FilterColumn;
import sk.uniba.grman19.models.entity.Source;

public interface SourceDAO {
	Optional<Source> getSource(Long id);

	Optional<Source> getSource(String name);

	List<Source> getSources(int offset, int limit, Map<FilterColumn, String> filters);

	long getSourceCount(Map<FilterColumn, String> filters);

	Source createSource(String name, String description);

	Source saveSource(Source source);

	List<Source> getSourcesByAttribute(String key, String value);
}
