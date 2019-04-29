package org.art.web.warrior.client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.art.web.warrior.commons.util.ParserUtil;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserCodeCompData {

    private String className;

    private String srcCode;

    public boolean isValid() {
        return StringUtils.isNotBlank(className)
                && StringUtils.isNotBlank(srcCode)
                && className.equals(ParserUtil.parseClassNameFromSrcString(srcCode));
    }
}
