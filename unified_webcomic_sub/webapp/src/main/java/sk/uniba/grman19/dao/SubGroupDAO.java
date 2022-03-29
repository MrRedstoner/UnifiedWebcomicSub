package sk.uniba.grman19.dao;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import sk.uniba.grman19.filter.FilterColumn;
import sk.uniba.grman19.models.entity.SubGroup;

public interface SubGroupDAO {
	Optional<SubGroup> getGroup(Long id);

	SubGroup createUserGroup();

	SubGroup createPublicGroup(String name, String description);

	List<SubGroup> getSubGroups(int offset, int limit, Map<FilterColumn, String> filters);

	long getSubGroupCount(Map<FilterColumn, String> filters);

	SubGroup saveGroup(SubGroup group);
}
