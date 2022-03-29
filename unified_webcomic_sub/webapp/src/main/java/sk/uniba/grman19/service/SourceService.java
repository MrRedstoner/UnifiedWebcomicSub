package sk.uniba.grman19.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sk.uniba.grman19.dao.SourceDAO;
import sk.uniba.grman19.filter.FilterColumn;
import sk.uniba.grman19.models.PaginatedList;
import sk.uniba.grman19.models.entity.Source;

@Service
public class SourceService {
	@Autowired
	private SourceDAO sourceDao;

	public PaginatedList<Source> getSources(int offset, int limit, Map<FilterColumn, String> filters) {
		long count = sourceDao.getSourceCount(filters);
		List<Source> items = sourceDao.getSources(offset, limit, filters);
		return new PaginatedList<>(count, items);
	}
}
