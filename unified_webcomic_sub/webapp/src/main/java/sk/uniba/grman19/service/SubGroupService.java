package sk.uniba.grman19.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sk.uniba.grman19.dao.SubGroupDAO;
import sk.uniba.grman19.filter.FilterColumn;
import sk.uniba.grman19.models.PaginatedList;
import sk.uniba.grman19.models.entity.SubGroup;

@Service
public class SubGroupService {
	@Autowired
	private SubGroupDAO subGroupDao;

	public PaginatedList<SubGroup> getSubGroups(int offset, int limit, Map<FilterColumn, String> filters) {
		long count = subGroupDao.getSubGroupCount(filters);
		List<SubGroup> items = subGroupDao.getSubGroups(offset, limit, filters);
		return new PaginatedList<>(count, items);
	}

}
