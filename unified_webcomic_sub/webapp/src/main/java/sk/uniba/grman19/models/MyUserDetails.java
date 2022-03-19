package sk.uniba.grman19.models;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

public class MyUserDetails extends User {
	/** generated */
	private static final long serialVersionUID = -4693349778493816926L;

	private final Long id;

	public MyUserDetails(Long id, String username, String password, String... authorities) {
		super(username, password, AuthorityUtils.createAuthorityList(authorities));
		this.id = id;
	}

	public Long getId() {
		return id;
	}

}
