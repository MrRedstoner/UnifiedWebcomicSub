package sk.uniba.grman19.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sk.uniba.grman19.dao.MailSettingsDAO;
import sk.uniba.grman19.dao.UWSUserDAO;
import sk.uniba.grman19.models.MyUserDetails;
import sk.uniba.grman19.models.UWSUser;
import sk.uniba.grman19.models.UserRegistration;

@Service
public class UWSUserService implements UserDetailsService {
	@Autowired
	private UWSUserDAO userDao;
	@Autowired
	private MailSettingsDAO mailSettingsDao;

	@Override
	public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
		Optional<UWSUser> ouser = userDao.getUser(name);
		UWSUser user = ouser.orElseThrow(() -> new UsernameNotFoundException("Invalid login"));
		// TODO roles
		return new MyUserDetails(user.getId(), user.getName(), user.getPassword(), "ANONYMOUS", "USER");
	}

	public Optional<UWSUser> getLoggedInUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			MyUserDetails user = (MyUserDetails) authentication.getPrincipal();
			return userDao.getUser(user.getId());
		}
		return Optional.empty();
	}

	@Transactional(readOnly = false)
	public UWSUser registerUser(UserRegistration user) {
		//TODO audit log creation
		UWSUser uuser = new UWSUser(user.getUsername(), user.getPassword(), false, false, false, false, false, false);
		uuser = userDao.createUWSUser(uuser);
		mailSettingsDao.createMailSettings(uuser.getId(), user.getEmail());
		return uuser;
	}
}
