package sk.uniba.grman19.rest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import sk.uniba.grman19.models.rest.UserRegistration;
import sk.uniba.grman19.service.UWSUserService;

@Controller
public class RegistrationController {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	private UWSUserService userDetailsService;
	@Autowired
	private AuthenticationManager authenticationManagerBean;
	
	@GetMapping("/registration")
	public String showRegistrationForm(WebRequest request, Model model) {
		LOGGER.trace("Registration");
		UserRegistration userDto = new UserRegistration();
		model.addAttribute("user", userDto);
		return "registration";
	}

	@PostMapping("/registration")
	public ModelAndView registerUserAccount(@ModelAttribute("user") @Valid UserRegistration userDto, BindingResult bindingResult, HttpServletRequest request) {
		if (bindingResult.hasErrors()) {
			return new ModelAndView("registration", "user", userDto);
		}

		try {
			LOGGER.info("Attempting registeration");
			userDetailsService.registerUser(userDto);
			autologin(userDto.getUsername(), userDto.getPassword());
		} catch (RuntimeException e) {
			ModelAndView mav = new ModelAndView("registration", "user", userDto);
			mav.addObject("message", e.getMessage());
			return mav;
		}

		return new ModelAndView("redirect:/index");
	}

	private void autologin(String username, String password) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

		Authentication auth = authenticationManagerBean.authenticate(usernamePasswordAuthenticationToken);

		if (auth.isAuthenticated()) {
			SecurityContextHolder.getContext().setAuthentication(auth);
			LOGGER.trace("Auto login successfull {}", username);
		}
	}
}