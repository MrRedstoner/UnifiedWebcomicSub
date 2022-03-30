package sk.uniba.grman19.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sk.uniba.grman19.dao.AuditLogDAO;
import sk.uniba.grman19.dao.SubscriptionDAO;
import sk.uniba.grman19.models.entity.GroupChild;
import sk.uniba.grman19.models.entity.SubGroup;
import sk.uniba.grman19.models.entity.UWSUser;

@Service
public class SubscriptionService {
	@Autowired
	private SubscriptionDAO subscriptionDao;
	@Autowired
	private AuditLogDAO auditLogDao;

	public Optional<GroupChild> getDirectSubscription(UWSUser user, SubGroup group) {
		return subscriptionDao.getGroupRelation(user.getMailSettings().getSubscribe(), group);
	}

	public void updateSubscription(UWSUser user, SubGroup group, boolean subscribed) {
		auditLogDao.saveLog(user, "Updated subscription to " + group.getId() + " to " + subscribed);
		if (subscribed) {
			subscriptionDao.addGroupRelation(user.getMailSettings().getSubscribe(), group);
		} else {
			subscriptionDao.removeGroupRelation(user.getMailSettings().getSubscribe(), group);
		}
	}
}
