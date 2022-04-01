package sk.uniba.grman19.rest;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonAlias;

import sk.uniba.grman19.dao.SourceDAO;
import sk.uniba.grman19.dao.SubGroupDAO;
import sk.uniba.grman19.filter.FilterColumn;
import sk.uniba.grman19.models.PaginatedList;
import sk.uniba.grman19.models.entity.GroupChild;
import sk.uniba.grman19.models.entity.Source;
import sk.uniba.grman19.models.entity.SubGroup;
import sk.uniba.grman19.models.entity.UWSUser;
import sk.uniba.grman19.models.rest.SubGroupUpdate;
import sk.uniba.grman19.service.SubGroupService;
import sk.uniba.grman19.service.SubscriptionService;
import sk.uniba.grman19.service.UWSUserService;
import sk.uniba.grman19.util.BadRequestException;
import sk.uniba.grman19.util.Cloner;
import sk.uniba.grman19.util.NotFoundException;

@RestController
@RequestMapping("/rest/group")
public class SubGroupRestController {
	private static Function<PaginatedList<SubGroup>, PaginatedList<SubGroup>> GROUPS = Cloner.clonePaginated();
	private static Function<SubGroup, SubGroup> GROUP = Cloner.clone("parents", "children.child", "sourceSubs.source");

	@Autowired
	private SubGroupService subGroupService;
	@Autowired
	private UWSUserService userDetailsService;
	@Autowired
	private SubscriptionService subscriptionService;
	@Autowired
	private SubGroupDAO subGroupDao;
	@Autowired
	private SourceDAO SourceDao;

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

		return GROUPS.apply(subGroups);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/readDetail", produces = MediaType.APPLICATION_JSON_VALUE)
	public SubGroup readDetail(@RequestParam(name = "id") Long id) {
		Optional<UWSUser> user = userDetailsService.getLoggedInUser();
		return readDetail(user, id);
	}

	@RequestMapping(method = RequestMethod.POST, path = "/saveDetail", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public SubGroup saveSubGroup(@RequestBody @Valid SubGroupUpdate update, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new BadRequestException(bindingResult);
		}

		UWSUser user = userDetailsService.requireEditGroup();
		SubGroup updated = subGroupService.updateSubGroup(user, update);

		return GROUP.apply(setDirectSub(updated, Optional.of(user)));
	}

	@RequestMapping(method = RequestMethod.POST, path = "/updateSub", produces = MediaType.APPLICATION_JSON_VALUE)
	public SubGroup updateSubscription(@RequestBody @Valid SubscriptionUpdate update, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new BadRequestException(bindingResult);
		}
		UWSUser user = userDetailsService.requireLoggedInUser();
		SubGroup group = subGroupDao.getNonUserGroup(update.getId()).orElseThrow(NotFoundException::new);
		subscriptionService.updateSubscription(user, group, update.getValue());

		return readDetail(Optional.of(user), update.getId());
	}

	@RequestMapping(method = RequestMethod.POST, path = "/updateChild", produces = MediaType.APPLICATION_JSON_VALUE)
	public SubGroup updateGroupChild(@RequestBody @Valid GroupChildUpdate update, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new BadRequestException(bindingResult);
		}
		if (update.getId().equals(update.getChild())) {
			throw new BadRequestException("Group can not be its own child");
		}
		UWSUser user = userDetailsService.requireEditGroup();
		SubGroup group = subGroupDao.getNonUserGroup(update.getId()).orElseThrow(NotFoundException::new);
		SubGroup child = subGroupDao.getNonUserGroup(update.getChild()).orElseThrow(NotFoundException::new);
		subscriptionService.updateGroupChild(user, group, child, update.getValue());

		return readDetail(Optional.of(user), update.getId());
	}

	@RequestMapping(method = RequestMethod.POST, path = "/updateSource", produces = MediaType.APPLICATION_JSON_VALUE)
	public SubGroup updateGroupSource(@RequestBody @Valid GroupChildUpdate update, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new BadRequestException(bindingResult);
		}
		UWSUser user = userDetailsService.requireEditGroup();
		SubGroup group = subGroupDao.getNonUserGroup(update.getId()).orElseThrow(NotFoundException::new);
		Source source = SourceDao.getSource(update.getChild()).orElseThrow(NotFoundException::new);
		subscriptionService.updateGroupSubscription(user, group, source, update.getValue());

		return readDetail(Optional.of(user), update.getId());
	}

	private SubGroup readDetail(Optional<UWSUser> user, Long id) {
		return subGroupDao.getNonUserGroup(id)
			.map(g -> setDirectSub(g, user))
			.map(GROUP)
			.orElseThrow(NotFoundException::new);
	}

	private SubGroup setDirectSub(SubGroup group, Optional<UWSUser> user) {
		List<GroupChild> subs = user.flatMap(u -> subscriptionService.getDirectSubscription(u, group))
			.map(Collections::singletonList)
			.orElse(Collections.emptyList());
		group.setParents(subs);
		return group;
	}

	@SuppressWarnings("unused") // false positive on some setters
	private static final class SubscriptionUpdate {
		@NotNull
		private Long id;
		@NotNull
		private Boolean value;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Boolean getValue() {
			return value;
		}

		public void setValue(Boolean value) {
			this.value = value;
		}
	}

	@SuppressWarnings("unused") // false positive on some setters
	private static final class GroupChildUpdate {
		@NotNull
		private Long id;
		@NotNull
		@JsonAlias("source")
		private Long child;
		@NotNull
		private Boolean value;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Long getChild() {
			return child;
		}

		public void setChild(Long child) {
			this.child = child;
		}

		public Boolean getValue() {
			return value;
		}

		public void setValue(Boolean value) {
			this.value = value;
		}
	}
}
