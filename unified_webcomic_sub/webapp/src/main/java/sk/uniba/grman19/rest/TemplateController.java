package sk.uniba.grman19.rest;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.WebRequest;

@Controller
public class TemplateController {
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@GetMapping({ "/index", "/sources", "/groups", "/settings", "/posts/**", "/mods/**" })
	public String showIndex(WebRequest request, Model model) {
		logger.trace("Index");
		return "index";
	}
}
