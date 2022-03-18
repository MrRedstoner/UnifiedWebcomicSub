package sk.uniba.grman19.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import sk.uniba.grman19.dao.UWSUserDAO;
import sk.uniba.grman19.models.UWSUser;

@Service
public class UWSUserDetailsService implements UserDetailsService {
	@Autowired
	private UWSUserDAO userDao;

	@Override
	public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
		Optional<UWSUser> ouser = userDao.getUser(name);
		UWSUser user = ouser.orElseThrow(() -> new RuntimeException("Invalid login"));
		return new User(user.getName(), user.getPassword(), AuthorityUtils.createAuthorityList("ANONYMOUS", "USER"));// TODO roles
	}

}
