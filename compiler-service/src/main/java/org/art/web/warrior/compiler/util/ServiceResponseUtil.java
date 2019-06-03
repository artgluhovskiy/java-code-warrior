package org.art.web.warrior.compiler.util;

import org.art.web.warrior.commons.compiler.dto.CompServiceResp;
import org.art.web.warrior.commons.compiler.dto.CompilationUnitResp;
import org.art.web.warrior.compiler.domain.CompilationResult;
import org.art.web.warrior.compiler.domain.CompilationUnit;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

public class ServiceResponseUtil {

    private ServiceResponseUtil() {
    }

    public static CompServiceResp buildCompilationResponse(CompilationResult result) {
        CompServiceResp.CompilationRespBuilder builder = CompServiceResp.builder();
        builder.compilerStatusCode(result.getCompStatus().getStatusCode())
                .compilerStatus(result.getCompStatus().getStatusId())
                .compUnitResults(result.getCompUnitResults());
        if (result.getCompStatus().getStatusCode() > 0) {
            builder.compUnitResults(result.getCompUnitResults());
        } else {
            builder.compilerMessage(result.getMessage().getCauseMessage())
                    .compilerErrorCode(result.getMessage().getErrorCode())
                    .errorCodeLine(result.getMessage().getCodeLine())
                    .errorColumnNumber(result.getMessage().getColumnNumber())
                    .errorPosition(result.getMessage().getPosition());
        }
        return builder.build();
    }

    public static CompServiceResp buildInternalServiceErrorResponse(Throwable cause, List<CompilationUnit> units) {
        CompServiceResp.CompilationRespBuilder builder = CompServiceResp.builder();
        builder.message(cause.getMessage());
        if (units != null) {
            Map<String, CompilationUnitResp> unitResults = units.stream()
                    .map(unit -> {
                        CompilationUnitResp unitResult = new CompilationUnitResp();
                        unitResult.setClassName(unit.getClassName());
                        unitResult.setSrcCode(unit.getSrcCode().toString());
                        return unitResult;
                    }).collect(toMap(CompilationUnitResp::getClassName, Function.identity()));
            builder.compUnitResults(unitResults);
        }
        return builder.build();
    }
}
