package org.art.web.warrior.client.config.validator;

import org.art.web.warrior.commons.util.ParserUtil;
import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ClassNameValidator implements ConstraintValidator<ClassNameMatchValidation, Object> {

    private String classNameField;

    private String srcCodeField;

    @Override
    public void initialize(ClassNameMatchValidation constraintAnnotation) {
        this.classNameField = constraintAnnotation.classNameField();
        this.srcCodeField = constraintAnnotation.srcCodeField();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        String className = (String) new BeanWrapperImpl(value).getPropertyValue(classNameField);
        String srcCode = (String) new BeanWrapperImpl(value).getPropertyValue(srcCodeField);
        return className != null && className.equals(ParserUtil.parseClassNameFromSrcString(srcCode));
    }
}
