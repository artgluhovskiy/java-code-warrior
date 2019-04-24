package org.art.web.warrior.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.lang.Nullable;

@Data
@ToString(exclude = {"compiledClassBytes"})
@AllArgsConstructor
@NoArgsConstructor
public class CompUnitResponse {

    private String className;

    private String srcCode;

    @Nullable
    private byte[] compiledClassBytes;
}
