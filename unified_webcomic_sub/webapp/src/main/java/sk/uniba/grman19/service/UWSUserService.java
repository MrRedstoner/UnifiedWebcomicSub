package sk.uniba.grman19.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sk.uniba.grman19.dao.AuditLogDAO;
import sk.uniba.grman19.dao.MailSettingsDAO;
import sk.uniba.grman19.dao.UWSUserDAO;
import sk.uniba.grman19.models.MyUserDetails;
import sk.uniba.grman19.models.entity.UWSUser;
import sk.uniba.grman19.models.rest.UserRegistration;
import sk.uniba.grman19.util.ForbiddenException;
import sk.uniba.grman19.util.PermissionCheck;

@Service
public class UWSUserService implements UserDetailsService {
	@Autowired
	private UWSUserDAO userDao;
	@Autowired
	private MailSettingsDAO mailSettingsDao;
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	private AuditLogDAO auditLogDao;

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

	/**
	 * @throws ForbiddenException
	 *             if the user is not logged in
	 */
	public UWSUser requireLoggedInUser() {
		return getLoggedInUser().orElseThrow(ForbiddenException::new);
	}

	/**
	 * @throws ForbiddenException
	 */
	public UWSUser requireEditGroup() {
		return getLoggedInUser().filter(PermissionCheck::canEditGroup).orElseThrow(ForbiddenException::new);
	}

	/**
	 * @throws ForbiddenException
	 */
	public UWSUser requireCreateSource() {
		return getLoggedInUser().filter(PermissionCheck::canCreateSource).orElseThrow(ForbiddenException::new);
	}

	/**
	 * @throws ForbiddenException
	 */
	public UWSUser requireEditSource() {
		return getLoggedInUser().filter(PermissionCheck::canEditSource).orElseThrow(ForbiddenException::new);
	}

	@Transactional(readOnly = false)
	public UWSUser registerUser(UserRegistration user) {
		UWSUser uuser = new UWSUser(user.getUsername(), passwordEncoder.encode(user.getPassword()));
		uuser = userDao.createUWSUser(uuser);
		mailSettingsDao.createMailSettings(uuser, user.getEmail());
		auditLogDao.saveLog(uuser, "Created user");
		return uuser;
	}
}
