package org.art.web.warrior.commons.compiler.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString(exclude = {"compiledClassBytes"})
@AllArgsConstructor
@NoArgsConstructor
public class CompilationUnitResp {

    private String className;

    private String srcCode;

    private byte[] compiledClassBytes;
}
