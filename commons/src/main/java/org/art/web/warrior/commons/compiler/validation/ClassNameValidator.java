package org.art.web.warrior.commons.compiler.validation;

import lombok.extern.slf4j.Slf4j;
import org.art.web.warrior.commons.util.ParserUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Slf4j
public class ClassNameValidator implements ConstraintValidator<ClassNameMatchValidation, Object> {

    private String classNameFieldName;

    private String srcCodeFieldName;

    @Override
    public void initialize(ClassNameMatchValidation constraintAnnotation) {
        this.classNameFieldName = constraintAnnotation.classNameField();
        this.srcCodeFieldName = constraintAnnotation.srcCodeField();
    }

    @Override
    public boolean isValid(Object codingTask, ConstraintValidatorContext context) {
        try {
            Field classNameField = codingTask.getClass().getDeclaredField(classNameFieldName);
            Field srcCodeField = codingTask.getClass().getDeclaredField(srcCodeFieldName);
            classNameField.setAccessible(true);
            srcCodeField.setAccessible(true);
            String className = (String) classNameField.get(codingTask);
            String srcCode = (String) srcCodeField.get(codingTask);
            return isNotBlank(className) && isNotBlank(srcCode) && className.equals(ParserUtil.parseClassNameFromSrcString(srcCode));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            log.warn("Cannot validate class names. Coding task data: {}", codingTask);
            return false;
        }
    }
}
