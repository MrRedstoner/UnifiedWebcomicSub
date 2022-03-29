package sk.uniba.grman19.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {
	/** generated */
	private static final long serialVersionUID = -858853756968313474L;

}
