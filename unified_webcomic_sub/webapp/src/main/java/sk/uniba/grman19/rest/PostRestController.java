package sk.uniba.grman19.rest;

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

import sk.uniba.grman19.filter.FilterColumn;
import sk.uniba.grman19.models.PaginatedList;
import sk.uniba.grman19.models.entity.Post;
import sk.uniba.grman19.models.entity.UWSUser;
import sk.uniba.grman19.service.PostService;
import sk.uniba.grman19.service.UWSUserService;
import sk.uniba.grman19.util.BadRequestException;
import sk.uniba.grman19.util.Cloner;
import sk.uniba.grman19.util.NotFoundException;

@RestController
@RequestMapping("/rest/post")
public class PostRestController {
	private static Function<PaginatedList<Post>, PaginatedList<Post>> POSTS = Cloner.clonePaginated();
	private static Function<Post, Post> POST = Cloner.clone("options");

	@Autowired
	private PostService postService;
	@Autowired
	private UWSUserService userDetailsService;

	@RequestMapping(method = RequestMethod.GET, path = "/read", produces = MediaType.APPLICATION_JSON_VALUE)
	public PaginatedList<Post> read(@RequestParam(name = "id") Optional<String> filterId, @RequestParam(name = "title") Optional<String> filterTitle,
			@RequestParam(name = "offset", defaultValue = "0") Integer offset, @RequestParam(name = "limit", defaultValue = "-1") Integer limit) {
		if (limit == -1) {
			limit = Integer.MAX_VALUE;
		}

		Map<FilterColumn, String> filters = new EnumMap<FilterColumn, String>(FilterColumn.class);
		filterId.ifPresent(s -> filters.put(FilterColumn.ID, s));
		filterTitle.ifPresent(s -> filters.put(FilterColumn.TITLE, s));
		PaginatedList<Post> posts = postService.getPosts(offset, limit, filters);

		return POSTS.apply(posts);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/readDetail", produces = MediaType.APPLICATION_JSON_VALUE)
	public Post readDetail(@RequestParam(name = "id") Long id) {
		// Optional<UWSUser> user = userDetailsService.getLoggedInUser();
		return postService.getPost(id).map(POST).orElseThrow(NotFoundException::new);
	}

	@RequestMapping(method = RequestMethod.POST, path = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Long createSource(@RequestBody @Valid NewPost post, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new BadRequestException(bindingResult);
		}

		UWSUser user = userDetailsService.requireCreatePost();
		Post updated = postService.createPost(user, post.getTitle(), post.getContent(), post.getOptions());
		return updated.getId();
	}

	@SuppressWarnings("unused") // false positive on some setters
	private static final class NewPost {
		@NotNull
		@NotEmpty
		private String title;
		@NotNull
		@NotEmpty
		private String content;
		@NotNull
		private List<@NotNull @NotEmpty String> options;

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public List<String> getOptions() {
			return options;
		}

		public void setOptions(List<String> options) {
			this.options = options;
		}
	}
}
