package sk.uniba.grman19.dao;

import java.util.Optional;

import sk.uniba.grman19.models.entity.GroupChild;
import sk.uniba.grman19.models.entity.SubGroup;

public interface SubscriptionDAO {
	GroupChild addGroupRelation(SubGroup parent, SubGroup child);

	Optional<GroupChild> getGroupRelation(SubGroup parent, SubGroup child);

	void removeGroupRelation(SubGroup parent, SubGroup child);
}
