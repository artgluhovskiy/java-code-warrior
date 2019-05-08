package org.art.web.warrior.commons.util;

import org.apache.commons.lang3.StringUtils;
import org.art.web.warrior.commons.CommonConstants;

import java.util.regex.Matcher;

public class ParserUtil {

    private ParserUtil() {
    }

    public static String parseClassNameFromSrcString(String srcCode) {
        String parsedClassName = StringUtils.EMPTY;
        if (StringUtils.isNotBlank(srcCode)) {
            Matcher matcher = CommonConstants.CLASS_NAME_REG_EXP.matcher(srcCode);
            if (matcher.find()) {
                parsedClassName = matcher.group(0);
            }
        }
        return parsedClassName;
    }
}
