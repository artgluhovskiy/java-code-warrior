package org.art.web.warrior.commons.compiler.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CompilationUnitReq {

    private String className;

    private String srcCode;
}
