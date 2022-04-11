package sk.uniba.grman19.service;

import static sk.uniba.grman19.processing.Constants.SIMPLE_POLL;
import static sk.uniba.grman19.processing.Constants.SOURCE_TYPE;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.javatuples.Triplet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sk.uniba.grman19.dao.AuditLogDAO;
import sk.uniba.grman19.dao.SourceAttributeDAO;
import sk.uniba.grman19.dao.SourceDAO;
import sk.uniba.grman19.dao.SourceUpdateDAO;
import sk.uniba.grman19.filter.FilterColumn;
import sk.uniba.grman19.models.PaginatedList;
import sk.uniba.grman19.models.entity.Source;
import sk.uniba.grman19.models.entity.SourceAttribute;
import sk.uniba.grman19.models.entity.SourceUpdate;
import sk.uniba.grman19.models.entity.UWSUser;
import sk.uniba.grman19.models.rest.NameDescriptionUpdate;
import sk.uniba.grman19.util.BadRequestException;
import sk.uniba.grman19.util.NotFoundException;
import sk.uniba.grman19.util.UpdateUtils;

@Service
public class SourceService {
	@Autowired
	private SourceDAO sourceDao;
	@Autowired
	private AuditLogDAO auditLogDao;
	@Autowired
	private SourceUpdateDAO sourceUpdateDao;
	@Autowired
	private SourceAttributeDAO sourceAttributeDao;

	public PaginatedList<Source> getSources(int offset, int limit, Map<FilterColumn, String> filters) {
		long count = sourceDao.getSourceCount(filters);
		List<Source> items = sourceDao.getSources(offset, limit, filters);
		return new PaginatedList<>(count, items);
	}

	@Transactional(readOnly = false)
	public Source createSource(UWSUser user, String name, String description) {
		if (sourceDao.getSource(name).isPresent()) {
			throw new BadRequestException("Source name must be unique");
		}
		Source source = sourceDao.createSource(name, description);
		auditLogDao.saveLog(user, "Created source " + source.getId());
		return source;
	}

	@Transactional(readOnly = false)
	public Source updateSource(UWSUser user, NameDescriptionUpdate update) {
		Source source = sourceDao.getSource(update.getId()).orElseThrow(NotFoundException::new);
		Optional<NameDescriptionUpdate> ondu = Optional.of(update);
		boolean nameUsed = ondu.map(NameDescriptionUpdate::getName)
			.flatMap(sourceDao::getSource)
			.isPresent();
		if (nameUsed) {
			throw new BadRequestException("Source name must be unique");
		}

		List<String> changes = new LinkedList<>();

		ondu.map(NameDescriptionUpdate::getName)
			.map(addChange("name", changes))
			.ifPresent(source::setName);
		ondu.map(NameDescriptionUpdate::getDescription)
			.map(addChange("description", changes))
			.ifPresent(source::setDescription);

		auditLogDao.saveLog(user, "Updated source " + update.getId() + " " + changes.toString());
		return sourceDao.saveSource(source);
	}

	public List<Source> getSimplePollSources() {
		return sourceDao.getSourcesByAttribute(SOURCE_TYPE, SIMPLE_POLL);
	}

	public List<SourceUpdate> getLastUpdates(Collection<Source> sources) {
		Set<Long> ids = sources.stream()
			.map(Source::getId)
			.collect(Collectors.toSet());
		return sourceUpdateDao.getLastUpdates(ids);
	}

	public void onSourceUpdate(Source source, String value) {
		sourceUpdateDao.saveSourceUpdate(source, value, new Date());
	}

	public Map<String, String> getProcessedAttributes(Source source) {
		return source.getSourceAttribute()
			.stream()
			.collect(Collectors.toMap(SourceAttribute::getName, SourceAttribute::getValue));
	}

	@Transactional(readOnly = false)
	public Map<String, String> updateSourceAttributes(UWSUser user, Source source, Map<String, String> attributes) {
		auditLogDao.saveLog(user, "Updates attributes for " + source.getId() + " to " + attributes.toString());

		Map<String, SourceAttribute> oldAttributes = source.getSourceAttribute()
			.stream()
			.collect(Collectors.toMap(SourceAttribute::getName, sa -> sa));

		Triplet<Map<String, String>, Map<String, String>, Set<String>> toUpdate = UpdateUtils.merge(oldAttributes, attributes);
		Map<String, String> create = toUpdate.getValue0();
		Map<String, String> update = toUpdate.getValue1();
		Set<String> delete = toUpdate.getValue2();
		sourceAttributeDao.updateAttributes(source, create, update, delete);

		return getProcessedAttributes(sourceDao.getSource(source.getId()).get());
	}

	private <O> Function<O, O> addChange(String fieldName, List<String> list) {
		return o -> {
			list.add(fieldName + "=" + o.toString());
			return o;
		};
	}
}
