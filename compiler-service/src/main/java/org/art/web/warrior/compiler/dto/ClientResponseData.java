package org.art.web.warrior.compiler.dto;

import lombok.*;
import org.art.web.warrior.compiler.domain.UnitResult;

import java.util.Map;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ClientResponseData {

    private int compilerStatusCode;

    private String compilerStatus;

    private String message;

    private String compilerMessage;

    private String compilerErrorCode;

    private long errorCodeLine;

    private long errorColumnNumber;

    private long errorPosition;

    Map<String, UnitResult> compUnitResults;
}
