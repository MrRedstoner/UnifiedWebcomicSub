package sk.uniba.grman19.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sk.uniba.grman19.dao.AuditLogDAO;
import sk.uniba.grman19.dao.SourceDAO;
import sk.uniba.grman19.filter.FilterColumn;
import sk.uniba.grman19.models.PaginatedList;
import sk.uniba.grman19.models.entity.Source;
import sk.uniba.grman19.models.entity.UWSUser;
import sk.uniba.grman19.models.rest.NameDescriptionUpdate;
import sk.uniba.grman19.util.BadRequestException;
import sk.uniba.grman19.util.NotFoundException;

@Service
public class SourceService {
	@Autowired
	private SourceDAO sourceDao;
	@Autowired
	private AuditLogDAO auditLogDao;

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

	private <O> Function<O, O> addChange(String fieldName, List<String> list) {
		return o -> {
			list.add(fieldName + "=" + o.toString());
			return o;
		};
	}
}
