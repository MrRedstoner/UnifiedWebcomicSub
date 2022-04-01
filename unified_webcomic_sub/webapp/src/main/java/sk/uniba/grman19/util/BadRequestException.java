package sk.uniba.grman19.util;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
	/** generated */
	private static final long serialVersionUID = 2457264703394433081L;

	public BadRequestException(BindingResult bindingResult) {
		this(bindingResult.getAllErrors()
			.iterator()
			.next()
			.getDefaultMessage());
	}

	public BadRequestException(String message) {
		super(message, null, true, false);
	}
}
