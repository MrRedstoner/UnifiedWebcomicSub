package sk.uniba.grman19.dao;

import java.util.List;
import java.util.Map;

import sk.uniba.grman19.filter.FilterColumn;
import sk.uniba.grman19.models.entity.SubGroup;

public interface SubGroupDAO {
	SubGroup createUserGroup();

	SubGroup createPublicGroup(String name, String description);

	List<SubGroup> getSubGroups(int offset, int limit, Map<FilterColumn, String> filters);

	long getSubGroupCount(Map<FilterColumn, String> filters);
}
