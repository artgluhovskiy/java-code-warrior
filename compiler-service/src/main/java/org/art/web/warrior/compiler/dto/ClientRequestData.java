package org.art.web.warrior.compiler.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;

import static org.art.web.warrior.compiler.ServiceCommonConstants.CLASS_NAME_REG_EXP;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ClientRequestData {

    private String className;

    private String srcCode;

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
