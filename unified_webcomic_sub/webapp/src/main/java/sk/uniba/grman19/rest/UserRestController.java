package sk.uniba.grman19.rest;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import sk.nociar.jpacloner.JpaCloner;
import sk.uniba.grman19.models.UWSUser;
import sk.uniba.grman19.service.UWSUserService;

@RestController
@RequestMapping("/rest/user")
public class UserRestController {

	@Autowired
	private UWSUserService userDetailsService;

	@RequestMapping(method = RequestMethod.GET, path = "/getlogged", produces = MediaType.APPLICATION_JSON_VALUE)
	public Optional<UWSUser> getLoggedInUser() {
		Optional<UWSUser> ouser = userDetailsService.getLoggedInUser();
		return ouser.map(JpaCloner::clone);
	}
}
