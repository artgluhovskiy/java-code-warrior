package org.art.web.warrior.commons.compiler.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = PasswordMatchesValidator.class)
public @interface PasswordMatches {

    String message() default "Passwords don't match!";

    String passwordField() default "password";

    String matchingPasswordField() default "matchingPassword";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
