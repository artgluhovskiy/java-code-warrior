package org.art.web.compiler.util;

import org.apache.commons.collections4.MapUtils;
import org.art.web.compiler.dto.ServiceResponseDto;
import org.art.web.compiler.model.api.CompilationResult;
import org.art.web.compiler.model.api.CompilationUnit;
import org.springframework.http.HttpStatus;

import java.util.Map;

public class ServiceResponseUtils {

    private ServiceResponseUtils() {
    }

    public static ServiceResponseDto buildInternalServiceErrorResponse(Throwable cause, CompilationUnit unit) {
        ServiceResponseDto.ServiceResponseDtoBuilder builder = ServiceResponseDto.builder();
        builder.serverResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        builder.message(cause.getMessage());
        if (unit != null) {
            builder.className(unit.getClassName())
                    .srcCode(unit.getSrcCode());
        }
        return builder.build();
    }

    public static ServiceResponseDto buildServiceResponse(CompilationResult result) {
        ServiceResponseDto.ServiceResponseDtoBuilder builder = ServiceResponseDto.builder();
        builder.serverResponseStatus(HttpStatus.OK.value());
        if (result != null) {
            builder.compilerStatusCode(result.getCompStatus().getStatusCode())
                    .compilerStatus(result.getCompStatus().getStatus());
            if (result.getCompStatus().getStatusCode() > 0) {
                builder.className(result.getClassName());
                Map<String, byte[]> compiledClassData = result.getCompiledClassData();
                if (MapUtils.isNotEmpty(compiledClassData)) {
                    byte[] compiledClass = compiledClassData.get(result.getClassName());
                    builder.compiledClass(compiledClass);
                }
            } else {
                builder.compilerMessage(result.getMessage().getCauseMessage())
                        .compilerErrorCode(result.getMessage().getErrorCode())
                        .errorCodeLine(result.getMessage().getCodeLine())
                        .errorColumnNumber(result.getMessage().getColumnNumber())
                        .errorPosition(result.getMessage().getPosition());
            }
        }
        return builder.build();
    }
}
