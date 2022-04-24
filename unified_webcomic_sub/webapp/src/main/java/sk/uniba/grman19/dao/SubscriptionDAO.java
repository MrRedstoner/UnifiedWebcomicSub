package sk.uniba.grman19.dao;

import java.util.Optional;

import sk.uniba.grman19.models.entity.GroupChild;
import sk.uniba.grman19.models.entity.PostSubscription;
import sk.uniba.grman19.models.entity.Source;
import sk.uniba.grman19.models.entity.SourceSubscription;
import sk.uniba.grman19.models.entity.SubGroup;
import sk.uniba.grman19.models.entity.UWSUser;

public interface SubscriptionDAO {
	GroupChild addGroupRelation(SubGroup parent, SubGroup child);

	Optional<GroupChild> getGroupRelation(SubGroup parent, SubGroup child);

	void removeGroupRelation(SubGroup parent, SubGroup child);

	SourceSubscription addSourceSubscription(SubGroup group, Source source);

	Optional<SourceSubscription> getSourceSubscription(SubGroup group, Source source);

	void removeSourceSubscription(SubGroup group, Source source);

	Optional<PostSubscription> getPostSubscription(SubGroup group, UWSUser poster);

	PostSubscription addPosterSubscription(SubGroup group, UWSUser poster);

	void removePosterSubscription(SubGroup group, UWSUser poster);
}
