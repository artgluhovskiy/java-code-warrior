package org.art.web.warrior.compiler.util;

import org.art.web.warrior.commons.compiler.dto.CompilationResponse;
import org.art.web.warrior.commons.compiler.dto.CompilationUnitDto;
import org.art.web.warrior.compiler.domain.CompilationResult;
import org.art.web.warrior.compiler.domain.CompilationUnit;

import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class ServiceUtil {

    private ServiceUtil() {
    }

    public static CompilationResponse buildCompilationResponse(CompilationResult result) {
        CompilationResponse.CompilationResponseBuilder builder = CompilationResponse.builder();
        builder.compilerStatusCode(result.getStatus().getStatusCode())
                .compilerStatus(result.getStatus().getStatusId());
        if (result.getStatus().getStatusCode() > 0) {
            Map<String, CompilationUnit> compUnits = result.getCompUnitResults();
            builder.compUnitResults(mapToCompUnitDtoMap(compUnits));
        } else {
            builder.compilerMessage(result.getMessage().getCauseMessage())
                    .compilerErrorCode(result.getMessage().getErrorCode())
                    .errorCodeLine(result.getMessage().getCodeLine())
                    .errorColumnNumber(result.getMessage().getColumnNumber())
                    .errorPosition(result.getMessage().getPosition());
        }
        return builder.build();
    }

    private static Map<String, CompilationUnitDto> mapToCompUnitDtoMap(Map<String, CompilationUnit> compUnits) {
        return compUnits.entrySet().stream()
                .collect(toMap(Map.Entry::getKey, entry -> {
                    CompilationUnit compUnit = entry.getValue();
                    CompilationUnitDto compUnitDto = new CompilationUnitDto(compUnit.getClassName(), compUnit.getSrcCode());
                    compUnitDto.setCompiledClassBytes(compUnit.getCompiledClassBytes());
                    return compUnitDto;
                }));
    }
}
