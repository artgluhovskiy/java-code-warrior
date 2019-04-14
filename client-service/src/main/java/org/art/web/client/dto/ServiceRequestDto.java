package org.art.web.client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ServiceRequestDto {

    private String className;
    private String src;

    public boolean isValid() {
        return StringUtils.isNotBlank(className)
                && StringUtils.isNotBlank(src);
    }
}
