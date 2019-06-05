package org.art.web.warrior.commons.compiler.dto;

import lombok.*;
import org.art.web.warrior.commons.compiler.validation.ClassNameMatchValidation;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ClassNameMatchValidation
public class CompilationUnitDto {

    @NotBlank(message = "Target class name should not be blank!")
    private String className;

    @NotBlank(message = "Source code should not be blank!")
    private String srcCode;

    private byte[] compiledClassBytes;
}
