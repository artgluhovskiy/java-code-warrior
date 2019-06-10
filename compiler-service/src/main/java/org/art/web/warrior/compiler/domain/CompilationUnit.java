package org.art.web.warrior.compiler.domain;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.art.web.warrior.commons.util.ParserUtil;

/**
 * Represents a model for compilation task, which contains all necessary information.
 * Contains java source code with a character sequence type.
 */
@Data
public class CompilationUnit {

    private final String className;

    private final String srcCode;

    private byte[] compiledClassBytes;

    public boolean isValid() {
        return StringUtils.isNotBlank(className)
            && StringUtils.isNotBlank(srcCode)
            && className.equals(ParserUtil.parseClassNameFromSrcString(srcCode));
    }
}
