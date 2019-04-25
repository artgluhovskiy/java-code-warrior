package org.art.web.warrior.compiler.util;

import org.art.web.warrior.commons.compiler.dto.CompServiceResponse;
import org.art.web.warrior.commons.compiler.dto.CompServiceUnitResponse;
import org.art.web.warrior.compiler.domain.CompilationResult;
import org.art.web.warrior.compiler.domain.CompilationUnit;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.Collections.singletonMap;
import static java.util.stream.Collectors.toMap;
import static org.art.web.warrior.compiler.ServiceCommonConstants.REQUEST_DATA_CANNOT_BE_PROCESSED_MESSAGE;

public class ServiceResponseUtils {

    private ServiceResponseUtils() {
    }

    public static CompServiceResponse buildClientResponse(CompilationResult result) {
        CompServiceResponse.CompServiceResponseBuilder builder = CompServiceResponse.builder();
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

    public static CompServiceResponse buildInternalServiceErrorResponse(Throwable cause, List<CompilationUnit> units) {
        CompServiceResponse.CompServiceResponseBuilder builder = CompServiceResponse.builder();
        builder.message(cause.getMessage());
        if (units != null) {
            Map<String, CompServiceUnitResponse> unitResults = units.stream()
                    .map(unit -> {
                        CompServiceUnitResponse unitResult = new CompServiceUnitResponse();
                        unitResult.setClassName(unit.getClassName());
                        unitResult.setSrcCode(unit.getSrcCode().toString());
                        return unitResult;
                    }).collect(toMap(CompServiceUnitResponse::getClassName, Function.identity()));
            builder.compUnitResults(unitResults);
        }
        return builder.build();
    }

    public static CompServiceResponse buildUnprocessableEntityResponse(String className, String src) {
        CompServiceResponse.CompServiceResponseBuilder builder = CompServiceResponse.builder();
        CompServiceUnitResponse unitResult = new CompServiceUnitResponse();
        unitResult.setClassName(className);
        unitResult.setSrcCode(src);
        builder.compUnitResults(singletonMap(className, unitResult))
                .message(REQUEST_DATA_CANNOT_BE_PROCESSED_MESSAGE);
        return builder.build();
    }
}
