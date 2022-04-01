package sk.uniba.grman19.configuration;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.databind.JsonMappingException;

import sk.uniba.grman19.util.BadRequestException;

@ControllerAdvice
public class ExceptionConfiguration {
	@ExceptionHandler(JsonMappingException.class)
	public ResponseEntity<?> handleConverterErrors(JsonMappingException exception) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BadRequestException("Deserialization failure"));
	}
}