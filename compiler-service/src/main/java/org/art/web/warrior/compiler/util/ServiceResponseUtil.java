package org.art.web.warrior.compiler.util;

import org.art.web.warrior.commons.compiler.dto.CompilationResponse;
import org.art.web.warrior.commons.compiler.dto.CompilationUnitDto;
import org.art.web.warrior.compiler.domain.CompilationResult;
import org.art.web.warrior.compiler.domain.CompilationUnit;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

public class ServiceResponseUtil {

    private ServiceResponseUtil() {
    }

    public static CompilationResponse buildCompilationResponse(CompilationResult result) {
        CompilationResponse.CompilationResponseBuilder builder = CompilationResponse.builder();
        builder.compilerStatusCode(result.getCompStatus().getStatusCode())
            .compilerStatus(result.getCompStatus().getStatusId());
        if (result.getCompStatus().getStatusCode() > 0) {
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

    public static CompilationResponse buildServiceErrorResponse(Throwable cause, List<CompilationUnit> compUnits) {
        Objects.requireNonNull(compUnits, "Compilation Unit list passed to the mapper should not be null!");
        return CompilationResponse.builder()
            .message(cause.getMessage())
            .compUnitResults(mapToCompUnitDtoList(compUnits))
            .build();
    }

    private static Map<String, CompilationUnitDto> mapToCompUnitDtoMap(Map<String, CompilationUnit> compUnits) {
        return compUnits.entrySet().stream()
            .collect(toMap(Map.Entry::getKey, entry -> {
                CompilationUnit compUnit = entry.getValue();
                return new CompilationUnitDto(compUnit.getClassName(), compUnit.getClassName(), compUnit.getCompiledClassBytes());
            }));
    }

    private static Map<String, CompilationUnitDto> mapToCompUnitDtoList(List<CompilationUnit> compUnits) {
        return compUnits.stream()
            .map(compUnit -> {
                CompilationUnitDto unitResult = new CompilationUnitDto();
                unitResult.setClassName(compUnit.getClassName());
                unitResult.setSrcCode(compUnit.getSrcCode().toString());
                return unitResult;
            }).collect(toMap(CompilationUnitDto::getClassName, Function.identity()));
    }
}
