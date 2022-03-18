package sk.uniba.grman19.dao;

import java.util.List;

import sk.uniba.grman19.models.Source;

public interface SourceDAO {
	Source getSource(int id);

	List<Source> getAllSources();
}
