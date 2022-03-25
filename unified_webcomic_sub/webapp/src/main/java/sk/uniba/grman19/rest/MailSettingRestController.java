package sk.uniba.grman19.rest;

import java.util.function.Function;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import sk.uniba.grman19.models.entity.MailSettings;
import sk.uniba.grman19.models.entity.UWSUser;
import sk.uniba.grman19.models.rest.MailSettingUpdate;
import sk.uniba.grman19.service.MailSettingsService;
import sk.uniba.grman19.service.UWSUserService;
import sk.uniba.grman19.util.Cloner;

@RestController
@RequestMapping("/rest/mail")
public class MailSettingRestController {
	private static Function<MailSettings, MailSettings> MAIL_SETTINGS = Cloner.clone();

	@Autowired
	private UWSUserService userDetailsService;
	@Autowired
	private MailSettingsService mailSettingsService;

	@RequestMapping(method = RequestMethod.GET, path = "/getmailset", produces = MediaType.APPLICATION_JSON_VALUE)
	public MailSettings getLoggedInMailSettings() {
		UWSUser user = userDetailsService.requireLoggedInUser();
		return MAIL_SETTINGS.apply(user.getMailSettings());
	}

	@RequestMapping(method = RequestMethod.POST, path = "/setmailset", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public MailSettings setLoggedInMailSettings(@RequestBody @Valid MailSettingUpdate update, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new RuntimeException(bindingResult.getAllErrors().get(0).getDefaultMessage());
		}

		UWSUser user = userDetailsService.requireLoggedInUser();
		mailSettingsService.updateUserMailSettings(user, update);

		return MAIL_SETTINGS.apply(user.getMailSettings());
	}
}
