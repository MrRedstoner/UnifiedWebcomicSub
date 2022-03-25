package sk.uniba.grman19.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import sk.uniba.grman19.models.rest.UserRegistration;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

	@Override
	public void initialize(PasswordMatches constraintAnnotation) {
	}

	@Override
	public boolean isValid(Object obj, ConstraintValidatorContext context) {
		UserRegistration user = (UserRegistration) obj;
		return user.getPassword().equals(user.getMatchingPassword());
	}
}