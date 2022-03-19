package sk.uniba.grman19.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.WebRequest;

@Controller
public class TemplateController {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@GetMapping("/index")
	public String showIndex(WebRequest request, Model model) {
		LOGGER.trace("Index");
		return "index";
	}
}
