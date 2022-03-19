package sk.uniba.grman19.validation;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {
	private static final String EMAIL_PATTERN = "^\\w+@\\w+([.\\w]+)*$";
	private static final Pattern EMAIL = Pattern.compile(EMAIL_PATTERN);

	@Override
	public void initialize(ValidEmail constraintAnnotation) {
	}

	@Override
	public boolean isValid(String email, ConstraintValidatorContext context) {
		return (validateEmail(email));
	}

	private boolean validateEmail(String email) {
		return EMAIL.matcher(email).matches();
	}
}