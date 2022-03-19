package sk.uniba.grman19.dao;

import java.util.Optional;

import sk.uniba.grman19.models.UWSUser;

public interface UWSUserDAO {
	Optional<UWSUser> getUser(Long id);

	Optional<UWSUser> getUser(String name);

	UWSUser createUWSUser(UWSUser user);
}
