package sk.uniba.grman19.rest;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import sk.uniba.grman19.models.entity.Post;
import sk.uniba.grman19.models.entity.UWSUser;
import sk.uniba.grman19.service.PostService;
import sk.uniba.grman19.service.UWSUserService;
import sk.uniba.grman19.util.BadRequestException;

@RestController
@RequestMapping("/rest/post")
public class PostRestController {
	@Autowired
	private PostService postService;
	@Autowired
	private UWSUserService userDetailsService;

	@RequestMapping(method = RequestMethod.POST, path = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Long createSource(@RequestBody @Valid NewPost post, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new BadRequestException(bindingResult);
		}

		UWSUser user = userDetailsService.requireCreatePost();
		Post updated = postService.createPost(user, post.getTitle(), post.getContent());
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
	}
}
