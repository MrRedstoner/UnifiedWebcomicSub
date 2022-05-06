package sk.uniba.grman19.validation;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {
	private static final String EMAIL_PATTERN = "^[^\\s@]+@\\w+([.\\w]+)*$";
	private static final Pattern EMAIL = Pattern.compile(EMAIL_PATTERN);

	@Override
	public void initialize(ValidEmail constraintAnnotation) {
	}

	@Override
	public boolean isValid(String email, ConstraintValidatorContext context) {
		if (email == null) {
			// validate with not null if you want
			return true;
		}
		return (validateEmail(email));
	}

	private boolean validateEmail(String email) {
		return EMAIL.matcher(email).matches();
	}
}