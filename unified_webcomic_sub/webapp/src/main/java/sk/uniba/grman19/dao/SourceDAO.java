package sk.uniba.grman19.dao;

import java.util.List;
import java.util.Optional;

import sk.uniba.grman19.models.Source;

public interface SourceDAO {
	Optional<Source> getSource(Long id);

	List<Source> getAllSources();
}
