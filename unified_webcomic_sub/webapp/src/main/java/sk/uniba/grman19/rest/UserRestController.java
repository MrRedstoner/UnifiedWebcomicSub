package sk.uniba.grman19.rest;

import java.util.EnumMap;
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

import sk.uniba.grman19.dao.UWSUserDAO;
import sk.uniba.grman19.filter.FilterColumn;
import sk.uniba.grman19.models.PaginatedList;
import sk.uniba.grman19.models.entity.PostSubscription;
import sk.uniba.grman19.models.entity.UWSUser;
import sk.uniba.grman19.models.rest.PermissionUpdate;
import sk.uniba.grman19.service.SubscriptionService;
import sk.uniba.grman19.service.UWSUserService;
import sk.uniba.grman19.util.BadRequestException;
import sk.uniba.grman19.util.Cloner;
import sk.uniba.grman19.util.NotFoundException;

@RestController
@RequestMapping("/rest/user")
public class UserRestController {
	private static Function<PaginatedList<UWSUser>, PaginatedList<UWSUser>> USERS = Cloner.clonePaginated();
	private static Function<UWSUser, UWSUser> WITH_MAIL_SETTINGS = Cloner.clone("mailSettings");
	private static Function<UWSUser, UWSUser> MODERATOR = Cloner.clone();
	private static Function<UWSUser, UWSUser> USER = Cloner.clone();

	@Autowired
	private UWSUserDAO userDao;
	@Autowired
	private UWSUserService userDetailsService;
	@Autowired
	private SubscriptionService subscriptionService;

	@RequestMapping(method = RequestMethod.GET, path = "/getlogged", produces = MediaType.APPLICATION_JSON_VALUE)
	public Optional<UWSUser> getLoggedInUser() {
		Optional<UWSUser> ouser = userDetailsService.getLoggedInUser();
		return ouser.map(WITH_MAIL_SETTINGS);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/readUsers", produces = MediaType.APPLICATION_JSON_VALUE)
	public PaginatedList<UWSUser> readUsers(@RequestParam(name = "id") Optional<String> filterId, @RequestParam(name = "name") Optional<String> filterName,
			@RequestParam(name = "offset", defaultValue = "0") Integer offset,
			@RequestParam(name = "limit", defaultValue = "-1") Integer limit) {
		if (limit == -1) {
			limit = Integer.MAX_VALUE;
		}

		userDetailsService.requireAdmin();

		Map<FilterColumn, String> filters = new EnumMap<FilterColumn, String>(FilterColumn.class);
		filterId.ifPresent(s -> filters.put(FilterColumn.ID, s));
		filterName.ifPresent(s -> filters.put(FilterColumn.NAME, s));
		
		PaginatedList<UWSUser> users = userDetailsService.getUsers(offset, limit, filters);

		return USERS.apply(users);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/readMods", produces = MediaType.APPLICATION_JSON_VALUE)
	public PaginatedList<UWSUser> readModerators(@RequestParam(name = "id") Optional<String> filterId, @RequestParam(name = "name") Optional<String> filterName,
			@RequestParam(name = "offset", defaultValue = "0") Integer offset,
			@RequestParam(name = "limit", defaultValue = "-1") Integer limit) {
		if (limit == -1) {
			limit = Integer.MAX_VALUE;
		}

		Map<FilterColumn, String> filters = new EnumMap<FilterColumn, String>(FilterColumn.class);
		filterId.ifPresent(s -> filters.put(FilterColumn.ID, s));
		filterName.ifPresent(s -> filters.put(FilterColumn.NAME, s));
		filters.put(FilterColumn.CAN_CREATE_POST, "1");
		PaginatedList<UWSUser> users = userDetailsService.getUsers(offset, limit, filters);

		return USERS.apply(users);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/readUserDetail", produces = MediaType.APPLICATION_JSON_VALUE)
	public UWSUser readDetail(@RequestParam(name = "id") Long id) {
		userDetailsService.requireAdmin();
		return userDao.getUser(id)
			.map(USER)
			.orElseThrow(NotFoundException::new);
	}

	@RequestMapping(method = RequestMethod.POST, path = "/updatePerms", produces = MediaType.APPLICATION_JSON_VALUE)
	public UWSUser updatePermissions(@RequestBody @Valid PermissionUpdate update, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new BadRequestException(bindingResult);
		}
		UWSUser user;
		if (update.getAdmin() != null) {
			user = userDetailsService.requireOwner();
		} else {
			user = userDetailsService.requireAdmin();
		}
		UWSUser editing = userDao.getUser(update.getId()).orElseThrow(NotFoundException::new);
		userDetailsService.updatePermissions(user, editing, update);
		return userDao.getUser(update.getId())
			.map(USER)
			.orElseThrow(NotFoundException::new);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/readModDetail", produces = MediaType.APPLICATION_JSON_VALUE)
	public UWSUser readModeratorDetail(@RequestParam(name = "id") Long id) {
		Optional<UWSUser> user = userDetailsService.getLoggedInUser();
		return readDetail(user, id);
	}

	@RequestMapping(method = RequestMethod.POST, path = "/updateModSub", produces = MediaType.APPLICATION_JSON_VALUE)
	public UWSUser updateSubscription(@RequestBody @Valid SubscriptionUpdate update, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new BadRequestException(bindingResult);
		}
		UWSUser user = userDetailsService.requireLoggedInUser();
		UWSUser moderator = userDao.getUser(update.getId()).orElseThrow(NotFoundException::new);
		subscriptionService.updateSubscription(user, moderator, update.getValue(), update.getSubscribe());

		return readDetail(Optional.of(user), update.getId());
	}

	private UWSUser readDetail(Optional<UWSUser> user, Long id){
		Optional<UWSUser> moderator = userDao.getUser(id);
		return moderator.map(mod -> setDirectSub(mod, user))
			.map(MODERATOR)
			.orElseThrow(NotFoundException::new);
	}

	private UWSUser setDirectSub(UWSUser moderator, Optional<UWSUser> user) {
		Optional<PostSubscription> subscribe = user.flatMap(u -> subscriptionService.getDirectSubscription(u, moderator));
		Optional<PostSubscription> ignore = user.flatMap(u -> subscriptionService.getDirectIgnore(u, moderator));
		moderator.setSubscribed(subscribe.isPresent());
		moderator.setIgnored(ignore.isPresent());
		return moderator;
	}

	@SuppressWarnings("unused") // false positive on some setters
	private static final class SubscriptionUpdate {
		@NotNull
		private Long id;
		@NotNull
		private Boolean value;
		@NotNull
		private Boolean subscribe;

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

		public Boolean getSubscribe() {
			return subscribe;
		}

		public void setSubscribe(Boolean subscribe) {
			this.subscribe = subscribe;
		}
	}
}
