package org.art.web.warrior.commons.compiler.validation;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Slf4j
public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    private String passwordFieldName;

    private String matchingPasswordFieldName;

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
        this.passwordFieldName = constraintAnnotation.passwordField();
        this.matchingPasswordFieldName = constraintAnnotation.matchingPasswordField();
    }

    @Override
    public boolean isValid(Object userCredentials, ConstraintValidatorContext context) {
        try {
            Field passwordField = userCredentials.getClass().getDeclaredField(passwordFieldName);
            Field matchingPasswordField = userCredentials.getClass().getDeclaredField(matchingPasswordFieldName);
            passwordField.setAccessible(true);
            matchingPasswordField.setAccessible(true);
            String password = (String) passwordField.get(userCredentials);
            String matchingPassword = (String) matchingPasswordField.get(userCredentials);
            return isNotBlank(password) && isNotBlank(matchingPassword) && password.equals(matchingPassword);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            log.warn("Cannot validate password fields. User data: {}", userCredentials);
            return false;
        }
    }
}
