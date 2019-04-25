package org.art.web.warrior.compiler.domain;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;

import static org.art.web.warrior.commons.CommonConstants.CLASS_NAME_REG_EXP;

/**
 * Represents a model for compilation task, which contains all necessary information.
 * Contains java source code with a character sequence type.
 */
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class CompilationUnit {

    private final String className;

    private final CharSequence srcCode;

    public String getClassName() {
        return className;
    }

    public CharSequence getSrcCode() {
        return srcCode;
    }

    private String parseClassNameFromSrc() {
        String parsedClassName = StringUtils.EMPTY;
        Matcher matcher = CLASS_NAME_REG_EXP.matcher(srcCode);
        if (matcher.find()) {
            parsedClassName = matcher.group(0);
        }
        return parsedClassName;
    }

    public boolean isValid() {
        return StringUtils.isNotBlank(className)
                && StringUtils.isNotBlank(srcCode)
                && className.equals(parseClassNameFromSrc());
    }
}
