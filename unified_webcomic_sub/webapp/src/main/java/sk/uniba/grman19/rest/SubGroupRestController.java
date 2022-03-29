package sk.uniba.grman19.rest;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sk.uniba.grman19.filter.FilterColumn;
import sk.uniba.grman19.models.PaginatedList;
import sk.uniba.grman19.models.entity.SubGroup;
import sk.uniba.grman19.service.SubGroupService;
import sk.uniba.grman19.util.Cloner;

@RestController
@RequestMapping("/rest/group")
public class SubGroupRestController {
	private static Function<PaginatedList<SubGroup>, PaginatedList<SubGroup>> SOURCES = Cloner.clonePaginated();
	@Autowired
	private SubGroupService subGroupService;

	@RequestMapping(method = RequestMethod.GET, path = "/read", produces = MediaType.APPLICATION_JSON_VALUE)
	public PaginatedList<SubGroup> read(@RequestParam(name = "id") Optional<String> filterId, @RequestParam(name = "name") Optional<String> filterName,
			@RequestParam(name = "description") Optional<String> filterDescription, @RequestParam(name = "offset", defaultValue = "0") Integer offset,
			@RequestParam(name = "limit", defaultValue = "-1") Integer limit) {
		if (limit == -1) {
			limit = Integer.MAX_VALUE;
		}

		Map<FilterColumn, String> filters = new EnumMap<FilterColumn, String>(FilterColumn.class);
		filterId.ifPresent(s -> filters.put(FilterColumn.ID, s));
		filterName.ifPresent(s -> filters.put(FilterColumn.NAME, s));
		filterDescription.ifPresent(s -> filters.put(FilterColumn.DESCRIPTION, s));
		filters.put(FilterColumn.USER_OWNED, "0");
		PaginatedList<SubGroup> subGroups = subGroupService.getSubGroups(offset, limit, filters);

		return SOURCES.apply(subGroups);
	}
}
