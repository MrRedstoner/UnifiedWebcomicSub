package sk.uniba.grman19.util;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException extends RuntimeException {
	/** generated */
	private static final long serialVersionUID = 9192258761545600693L;
}