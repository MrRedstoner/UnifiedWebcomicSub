package sk.uniba.grman19.dao.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import sk.uniba.grman19.dao.MailSettingsDAO;
import sk.uniba.grman19.dao.SubGroupDAO;
import sk.uniba.grman19.filter.FilterColumn;
import sk.uniba.grman19.filter.FilterMapper;
import sk.uniba.grman19.models.entity.MailSettings;
import sk.uniba.grman19.models.entity.MailSettings_;
import sk.uniba.grman19.models.entity.SubGroup;
import sk.uniba.grman19.models.entity.UWSUser;
import sk.uniba.grman19.util.query.FilterMapperQuery;

@Component
@Transactional(readOnly = true)
public class MailSettingsDAOImpl implements MailSettingsDAO {
	private static final Date DATE_NEVER = new Date(0l);

	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	@Qualifier("mailSettingQueryByFilter")
	private FilterMapperQuery<MailSettings> queryByFilter;
	@Autowired
	private SubGroupDAO subGroupDao;

	@Override
	@Transactional(readOnly = false)
	public MailSettings createMailSettings(UWSUser user, String email) {
		SubGroup subscribe = subGroupDao.createUserGroup();
		SubGroup ignore = subGroupDao.createUserGroup();
		MailSettings settings = new MailSettings(user, email, subscribe, ignore, false, false, (byte) 0, DATE_NEVER, DATE_NEVER);
		entityManager.persist(settings);
		return settings;
	}

	@Override
	@Transactional(readOnly = false)
	public void saveMailSettings(MailSettings settings) {
		entityManager.merge(settings);
	}

	@Override
	public List<MailSettings> getActiveDailyMail() {
		return queryByFilter.queryList(0, Integer.MAX_VALUE, Collections.singletonMap(FilterColumn.DAILY, "1"));
	}

	private static FilterMapper makeFilterMapper(CriteriaBuilder cb, Root<MailSettings> root) {
		return new FilterMapper(cb)
			.addBooleanFilter(FilterColumn.DAILY, root.get(MailSettings_.daily));
	}

	@Configuration
	static class Config {
		@Bean(name = "mailSettingQueryByFilter")
		@PersistenceContext
		FilterMapperQuery<MailSettings> queryByFilter(EntityManager entityManager) {
			return new FilterMapperQuery<>(entityManager, MailSettings.class, MailSettingsDAOImpl::makeFilterMapper);
		}
	}
}
