package sk.uniba.grman19.rest;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sk.uniba.grman19.dao.SourceDAO;
import sk.uniba.grman19.filter.FilterColumn;
import sk.uniba.grman19.models.PaginatedList;
import sk.uniba.grman19.models.entity.Source;
import sk.uniba.grman19.models.entity.SourceSubscription;
import sk.uniba.grman19.models.entity.UWSUser;
import sk.uniba.grman19.models.rest.NameDescriptionUpdate;
import sk.uniba.grman19.service.SourceService;
import sk.uniba.grman19.service.SubscriptionService;
import sk.uniba.grman19.service.UWSUserService;
import sk.uniba.grman19.util.BadRequestException;
import sk.uniba.grman19.util.Cloner;
import sk.uniba.grman19.util.NotFoundException;

@RestController
@RequestMapping("/rest/source")
public class SourceRestController {
	private static Function<PaginatedList<Source>, PaginatedList<Source>> SOURCES = Cloner.clonePaginated();
	private static Function<Source, Source> SOURCE = Cloner.clone("sourceSubs");

	@Autowired
	private SourceDAO sourceDao;
	@Autowired
	private SourceService sourceService;
	@Autowired
	private UWSUserService userDetailsService;
	@Autowired
	private SubscriptionService subscriptionService;

	@RequestMapping(method = RequestMethod.GET, path = "/read", produces = MediaType.APPLICATION_JSON_VALUE)
	public PaginatedList<Source> read(@RequestParam(name = "id") Optional<String> filterId, @RequestParam(name = "name") Optional<String> filterName,
			@RequestParam(name = "description") Optional<String> filterDescription, @RequestParam(name = "offset", defaultValue = "0") Integer offset,
			@RequestParam(name = "limit", defaultValue = "-1") Integer limit) {
		if (limit == -1) {
			limit = Integer.MAX_VALUE;
		}

		Map<FilterColumn, String> filters = new EnumMap<FilterColumn, String>(FilterColumn.class);
		filterId.ifPresent(s -> filters.put(FilterColumn.ID, s));
		filterName.ifPresent(s -> filters.put(FilterColumn.NAME, s));
		filterDescription.ifPresent(s -> filters.put(FilterColumn.DESCRIPTION, s));
		PaginatedList<Source> sources = sourceService.getSources(offset, limit, filters);

		return SOURCES.apply(sources);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/readDetail", produces = MediaType.APPLICATION_JSON_VALUE)
	public Source readDetail(@RequestParam(name = "id") Long id) {
		Optional<UWSUser> user = userDetailsService.getLoggedInUser();
		return readDetail(user, id);
	}

	@RequestMapping(method = RequestMethod.POST, path = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Long createSource(@RequestBody @Valid NewSource source, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new BadRequestException(bindingResult);
		}

		UWSUser user = userDetailsService.requireCreateSource();
		Source updated = sourceService.createSource(user, source.getName(), source.getDescription());
		return updated.getId();
	}

	@RequestMapping(method = RequestMethod.POST, path = "/saveDetail", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Source saveSource(@RequestBody @Valid NameDescriptionUpdate update, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new BadRequestException(bindingResult);
		}

		UWSUser user = userDetailsService.requireEditSource();
		Source updated = sourceService.updateSource(user, update);

		return SOURCE.apply(setDirectSub(updated, Optional.of(user)));
	}

	@RequestMapping(method = RequestMethod.POST, path = "/updateSub", produces = MediaType.APPLICATION_JSON_VALUE)
	public Source updateSubscription(@RequestBody @Valid SubscriptionUpdate update, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new BadRequestException(bindingResult);
		}
		UWSUser user = userDetailsService.requireLoggedInUser();
		Source source = sourceDao.getSource(update.getId()).orElseThrow(NotFoundException::new);
		subscriptionService.updateSubscription(user, source, update.getValue());

		return readDetail(Optional.of(user), update.getId());
	}

	private Source readDetail(Optional<UWSUser> user, Long id) {
		return sourceDao.getSource(id)
			.map(g -> setDirectSub(g, user))
			.map(SOURCE)
			.orElseThrow(NotFoundException::new);
	}

	private Source setDirectSub(Source source, Optional<UWSUser> user) {
		List<SourceSubscription> subs = user.flatMap(u -> subscriptionService.getDirectSubscription(u, source))
			.map(Collections::singletonList)
			.orElse(Collections.emptyList());
		source.setSourceSubs(subs);
		return source;
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
	private static final class NewSource {
		@NotNull
		@NotEmpty
		private String name;
		@NotNull
		@NotEmpty
		private String description;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}
	}
}
