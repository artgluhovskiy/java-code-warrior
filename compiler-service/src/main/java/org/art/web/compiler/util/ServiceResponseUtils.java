package org.art.web.compiler.util;

import org.art.web.compiler.dto.ServiceResponseDto;
import org.art.web.compiler.model.api.CompilationResult;
import org.art.web.compiler.model.api.CompilationUnit;

public class ServiceResponseUtils {

    private ServiceResponseUtils() {
    }

    public static ServiceResponseDto buildInternalServiceErrorResponse(Throwable cause, CompilationUnit unit) {
        ServiceResponseDto.ServiceResponseDtoBuilder builder = ServiceResponseDto.builder();
        builder.message(cause.getMessage());
        if (unit != null) {
            builder.className(unit.getClassName())
                    .srcCode(unit.getSrcCode());
        }
        return builder.build();
    }

    public static ServiceResponseDto buildCompServiceResponse(CompilationResult result) {
        ServiceResponseDto.ServiceResponseDtoBuilder builder = ServiceResponseDto.builder();
            builder.compilerStatusCode(result.getCompStatus().getStatusCode())
                    .compilerStatus(result.getCompStatus().getStatus())
                    .className(result.getClassName())
                    .srcCode(result.getSrcCode());
            if (result.getCompStatus().getStatusCode() > 0) {
                builder.compiledClass(result.getCompiledClassBytes());
            } else {
                builder.compilerMessage(result.getMessage().getCauseMessage())
                        .compilerErrorCode(result.getMessage().getErrorCode())
                        .errorCodeLine(result.getMessage().getCodeLine())
                        .errorColumnNumber(result.getMessage().getColumnNumber())
                        .errorPosition(result.getMessage().getPosition());
            }
        return builder.build();
    }

    public static ServiceResponseDto buildUnpocessableEntityResponse(String className, String src) {
        ServiceResponseDto.ServiceResponseDtoBuilder builder = ServiceResponseDto.builder();
        builder.className(className)
                .srcCode(src)
                .message("Invalid request data!");
        return builder.build();
    }
}
