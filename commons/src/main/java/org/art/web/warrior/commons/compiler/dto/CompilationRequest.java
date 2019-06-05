package org.art.web.warrior.commons.compiler.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CompilationRequest {

    @Valid
    private List<CompilationUnitDto> compUnits;
}
