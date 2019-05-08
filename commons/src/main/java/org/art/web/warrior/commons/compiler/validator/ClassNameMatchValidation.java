package org.art.web.warrior.commons.compiler.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ClassNameValidator.class)
public @interface ClassNameMatchValidation {

    String message() default "Class name in the source code doesn't equal to the class name in the field!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String classNameField() default "className";

    String srcCodeField() default "srcCode";
}
