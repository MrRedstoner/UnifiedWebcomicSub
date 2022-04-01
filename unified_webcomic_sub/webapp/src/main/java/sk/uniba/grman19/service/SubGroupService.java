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
import sk.uniba.grman19.dao.SubGroupDAO;
import sk.uniba.grman19.filter.FilterColumn;
import sk.uniba.grman19.models.PaginatedList;
import sk.uniba.grman19.models.entity.SubGroup;
import sk.uniba.grman19.models.entity.UWSUser;
import sk.uniba.grman19.models.rest.SubGroupUpdate;
import sk.uniba.grman19.util.BadRequestException;
import sk.uniba.grman19.util.NotFoundException;

@Service
public class SubGroupService {
	@Autowired
	private SubGroupDAO subGroupDao;
	@Autowired
	private AuditLogDAO auditLogDao;

	public PaginatedList<SubGroup> getSubGroups(int offset, int limit, Map<FilterColumn, String> filters) {
		long count = subGroupDao.getSubGroupCount(filters);
		List<SubGroup> items = subGroupDao.getSubGroups(offset, limit, filters);
		return new PaginatedList<>(count, items);
	}

	@Transactional(readOnly = false)
	public SubGroup updateSubGroup(UWSUser user, SubGroupUpdate update) {
		SubGroup group = subGroupDao.getNonUserGroup(update.getId()).orElseThrow(NotFoundException::new);
		Optional<SubGroupUpdate> osgu = Optional.of(update);
		boolean nameUsed = osgu.map(SubGroupUpdate::getName)
			.flatMap(subGroupDao::getGroup)
			.isPresent();
		if(nameUsed){
			throw new BadRequestException("Group name must be unique");
		}

		List<String> changes = new LinkedList<>();

		osgu.map(SubGroupUpdate::getName)
			.map(addChange("name", changes))
			.ifPresent(group::setName);
		osgu.map(SubGroupUpdate::getDescription)
			.map(addChange("description", changes))
			.ifPresent(group::setDescription);

		auditLogDao.saveLog(user, "Updated group " + update.getId() + " " + changes.toString());
		return subGroupDao.saveGroup(group);
	}

	@Transactional(readOnly = false)
	public SubGroup createSubGroup(UWSUser user, String name, String description) {
		if (subGroupDao	.getGroup(name).isPresent()) {
			throw new BadRequestException("Group name must be unique");
		}
		SubGroup group = subGroupDao.createPublicGroup(name, description);
		auditLogDao.saveLog(user, "Created group " + group.getId());
		return group;
	}

	private <O> Function<O, O> addChange(String fieldName, List<String> list) {
		return o -> {
			list.add(fieldName + "=" + o.toString());
			return o;
		};
	}
}
