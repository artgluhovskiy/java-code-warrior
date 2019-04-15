package org.art.web.warrior.compiler.model;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.art.web.warrior.compiler.model.api.CompilationUnit;

/**
 * Represents a model for compilation task, which contains all necessary information.
 * Contains java source code with a character sequence type.
 */
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class CharSeqCompilationUnit implements CompilationUnit<CharSequence> {

    private final String className;

    private final CharSequence srcCode;

    @Override
    public String getClassName() {
        return className;
    }

    @Override
    public CharSequence getSrcCode() {
        return srcCode;
    }

    @Override
    public boolean isValid() {
        return StringUtils.isNotBlank(className)
                && StringUtils.isNotBlank(srcCode);
    }
}
