package org.art.web.warrior.commons.compiler.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.art.web.warrior.commons.CommonConstants;

import java.util.regex.Matcher;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CompServiceUnitRequest {

    private String className;

    private String srcCode;

    private String parseClassNameFromSrc() {
        String parsedClassName = StringUtils.EMPTY;
        Matcher matcher = CommonConstants.CLASS_NAME_REG_EXP.matcher(srcCode);
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
