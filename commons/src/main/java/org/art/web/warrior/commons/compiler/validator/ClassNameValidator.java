package org.art.web.warrior.commons.compiler.validator;

import lombok.extern.slf4j.Slf4j;
import org.art.web.warrior.commons.util.ParserUtil;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
public class ClassNameValidator implements ConstraintValidator<ClassNameMatchValidation, Object> {

    private String classNameField;

    private String srcCodeField;

    @Override
    public void initialize(ClassNameMatchValidation constraintAnnotation) {
        this.classNameField = constraintAnnotation.classNameField();
        this.srcCodeField = constraintAnnotation.srcCodeField();
    }

    @Override
    public boolean isValid(Object codingTask, ConstraintValidatorContext context) {
        BeanWrapper taskBeanWrapper = new BeanWrapperImpl(codingTask);
        if (taskBeanWrapper.isReadableProperty(classNameField) && taskBeanWrapper.isReadableProperty(srcCodeField)) {
            String className = (String) taskBeanWrapper.getPropertyValue(classNameField);
            String srcCode = (String) taskBeanWrapper.getPropertyValue(srcCodeField);
            return className != null && className.equals(ParserUtil.parseClassNameFromSrcString(srcCode));
        } else {
            log.warn("Cannot find 'classNameField' or 'srcCodeField' object properties during class name validation! Object: {}", codingTask);
            return false;
        }
    }
}
