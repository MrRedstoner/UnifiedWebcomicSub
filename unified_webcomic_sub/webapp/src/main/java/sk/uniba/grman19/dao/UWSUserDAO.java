package sk.uniba.grman19.dao;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import sk.uniba.grman19.filter.FilterColumn;
import sk.uniba.grman19.models.entity.SubGroup;
import sk.uniba.grman19.models.entity.UWSUser;

public interface UWSUserDAO {
	Optional<UWSUser> getUser(Long id);

	Optional<UWSUser> getUser(String name);

	UWSUser createUWSUser(UWSUser user);

	UWSUser saveUWSUser(UWSUser user);

	List<UWSUser> resolvePosters(SubGroup subscribe, SubGroup ignore);

	long getUserCount(Map<FilterColumn, String> filters);

	List<UWSUser> getUsers(Integer offset, Integer limit, Map<FilterColumn, String> filters);
}
