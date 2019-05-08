package org.art.web.warrior.commons.compiler.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.art.web.warrior.commons.compiler.validator.ClassNameMatchValidation;

import javax.validation.constraints.NotBlank;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ClassNameMatchValidation
public class CompilationUnitReq {

    @NotBlank(message = "Target class name should not be blank!")
    private String className;

    @NotBlank(message = "Source code should not be blank!")
    private String srcCode;
}
