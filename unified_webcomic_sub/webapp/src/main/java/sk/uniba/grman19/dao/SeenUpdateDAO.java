package sk.uniba.grman19.dao;

import sk.uniba.grman19.models.entity.SeenUpdate;
import sk.uniba.grman19.models.entity.SourceUpdate;
import sk.uniba.grman19.models.entity.UWSUser;

public interface SeenUpdateDAO {
	SeenUpdate createSeenUpdate(UWSUser user, SourceUpdate update);

	SeenUpdate updateSeenUpdate(UWSUser user, SourceUpdate update);
}
