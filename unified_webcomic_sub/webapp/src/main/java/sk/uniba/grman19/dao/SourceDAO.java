package sk.uniba.grman19.dao;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import sk.uniba.grman19.filter.FilterColumn;
import sk.uniba.grman19.models.entity.Source;

public interface SourceDAO {
	Optional<Source> getSource(Long id);

	List<Source> getSources(int offset, int limit, Map<FilterColumn, String> filters);

	long getSourceCount(Map<FilterColumn, String> filters);
}
