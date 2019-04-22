package org.art.web.warrior.compiler.util;

import org.art.web.warrior.compiler.dto.ClientResponseData;
import org.art.web.warrior.compiler.domain.CompilationResult;
import org.art.web.warrior.compiler.domain.CompilationUnit;
import org.art.web.warrior.compiler.domain.UnitResult;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.Collections.singletonMap;
import static java.util.stream.Collectors.toMap;
import static org.art.web.warrior.compiler.ServiceCommonConstants.REQUEST_DATA_CANNOT_BE_PROCESSED_MESSAGE;

public class ServiceResponseUtils {

    private ServiceResponseUtils() {
    }

    public static ClientResponseData buildClientResponse(CompilationResult result) {
        ClientResponseData.ClientResponseDataBuilder builder = ClientResponseData.builder();
        builder.compilerStatusCode(result.getCompStatus().getStatusCode())
                .compilerStatus(result.getCompStatus().getStatus())
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

    public static ClientResponseData buildInternalServiceErrorResponse(Throwable cause, List<CompilationUnit> units) {
        ClientResponseData.ClientResponseDataBuilder builder = ClientResponseData.builder();
        builder.message(cause.getMessage());
        if (units != null) {
            Map<String, UnitResult> unitResults = units.stream()
                    .map(unit -> {
                        UnitResult unitResult = new UnitResult();
                        unitResult.setClassName(unit.getClassName());
                        unitResult.setSrcCode(unit.getSrcCode());
                        return unitResult;
                    }).collect(toMap(UnitResult::getClassName, Function.identity()));
            builder.compUnitResults(unitResults);
        }
        return builder.build();
    }

    public static ClientResponseData buildUnprocessableEntityResponse(String className, String src) {
        ClientResponseData.ClientResponseDataBuilder builder = ClientResponseData.builder();
        UnitResult unitResult = new UnitResult();
        unitResult.setClassName(className);
        unitResult.setSrcCode(src);
        builder.compUnitResults(singletonMap(className, unitResult))
                .message(REQUEST_DATA_CANNOT_BE_PROCESSED_MESSAGE);
        return builder.build();
    }
}
