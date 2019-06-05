package org.art.web.warrior.compiler.domain;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;

import static org.art.web.warrior.commons.CommonConstants.CLASS_NAME_REG_EXP;

/**
 * Represents a model for compilation task, which contains all necessary information.
 * Contains java source code with a character sequence type.
 */
@Data
public class CompilationUnit {

    private final String className;

    private final CharSequence srcCode;

    private byte[] compiledClassBytes;

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
