package sk.uniba.grman19.dao;

import java.util.Date;
import java.util.List;
import java.util.Set;

import sk.uniba.grman19.models.entity.Source;
import sk.uniba.grman19.models.entity.SourceUpdate;

public interface SourceUpdateDAO {
	void saveSourceUpdate(Source source, String value, Date date);

	List<SourceUpdate> getLastUpdates(Set<Long> ids);
}
