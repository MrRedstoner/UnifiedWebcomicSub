package sk.uniba.grman19.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sk.uniba.grman19.dao.AuditLogDAO;
import sk.uniba.grman19.dao.SubscriptionDAO;
import sk.uniba.grman19.models.entity.GroupChild;
import sk.uniba.grman19.models.entity.Source;
import sk.uniba.grman19.models.entity.SourceSubscription;
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

	@Transactional(readOnly = false)
	public void updateSubscription(UWSUser user, SubGroup group, boolean subscribed) {
		auditLogDao.saveLog(user, "Updated subscription to group " + group.getId() + " to " + subscribed);
		if (subscribed) {
			subscriptionDao.addGroupRelation(user.getMailSettings().getSubscribe(), group);
		} else {
			subscriptionDao.removeGroupRelation(user.getMailSettings().getSubscribe(), group);
		}
	}

	@Transactional(readOnly = false)
	public void updateGroupChild(UWSUser user, SubGroup group, SubGroup child, boolean value) {
		auditLogDao.saveLog(user, "Updated group " + group.getId() + " child " + child.getId() + " to " + value);
		if (value) {
			subscriptionDao.addGroupRelation(group, child);
		} else {
			subscriptionDao.removeGroupRelation(group, child);
		}
	}

	@Transactional(readOnly = false)
	public void updateGroupSubscription(UWSUser user, SubGroup group, Source source, boolean value) {
		auditLogDao.saveLog(user, "Updated group " + group.getId() + " source " + source.getId() + " to " + value);
		if (value) {
			subscriptionDao.addSourceSubscription(group, source);
		} else {
			subscriptionDao.removeSourceSubscription(group, source);
		}
	}

	public void updateGroupSubscription(UWSUser user, SubGroup group, UWSUser poster, boolean value) {
		auditLogDao.saveLog(user, "Updated group " + group.getId() + " poster " + poster.getId() + " to " + value);
		if (value) {
			subscriptionDao.addPosterSubscription(group, poster);
		} else {
			subscriptionDao.removePosterSubscription(group, poster);
		}
	}

	@Transactional(readOnly = false)
	public void updateSubscription(UWSUser user, Source source, boolean value, boolean subscribe) {
		SubGroup group;
		if (subscribe) {
			auditLogDao.saveLog(user, "Updated subscription to source " + source.getId() + " to " + value);
			group = user.getMailSettings().getSubscribe();
		} else {
			auditLogDao.saveLog(user, "Updated ignore to source " + source.getId() + " to " + value);
			group = user.getMailSettings().getIgnore();
		}
		if (value) {
			subscriptionDao.addSourceSubscription(group, source);
		} else {
			subscriptionDao.removeSourceSubscription(group, source);
		}
	}

	public Optional<SourceSubscription> getDirectSubscription(UWSUser user, Source source) {
		return subscriptionDao.getSourceSubscription(user.getMailSettings().getSubscribe(), source);
	}

	public Optional<SourceSubscription> getDirectIgnore(UWSUser user, Source source) {
		return subscriptionDao.getSourceSubscription(user.getMailSettings().getIgnore(), source);
	}
}
