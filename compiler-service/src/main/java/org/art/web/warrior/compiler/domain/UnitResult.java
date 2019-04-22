package org.art.web.warrior.compiler.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.lang.Nullable;

@Data
@ToString(exclude = {"compiledClassBytes"})
@AllArgsConstructor
@NoArgsConstructor
public class UnitResult {

    private String className;

    private Object srcCode;

    @Nullable
    private byte[] compiledClassBytes;
}
