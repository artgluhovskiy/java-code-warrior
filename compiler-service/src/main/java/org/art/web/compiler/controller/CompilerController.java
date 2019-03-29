package org.art.web.compiler.controller;

import org.apache.commons.lang3.StringUtils;
import org.art.web.compiler.dto.ServiceResponseDto;
import org.art.web.compiler.exceptions.CompilationServiceException;
import org.art.web.compiler.model.CharSeqCompilationUnit;
import org.art.web.compiler.model.api.CompilationResult;
import org.art.web.compiler.model.api.CompilationUnit;
import org.art.web.compiler.service.api.CompilationService;
import org.art.web.compiler.util.ServiceResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import static org.art.web.compiler.service.ServiceCommonConstants.COMPILER_SERVICE_OK_MESSAGE;
import static org.art.web.compiler.service.ServiceCommonConstants.KRYO_CONTENT_TYPE;

@RestController
@RequestMapping(value = "/compile", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, KRYO_CONTENT_TYPE})
public class CompilerController {

    private static final Logger LOG = LoggerFactory.getLogger(CompilerController.class);

    private CompilationService compilationService;

    @Autowired
    public CompilerController(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @RequestMapping(value = "/src", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity compile(@RequestParam(value = "classname", required = false) String className,
                                  @RequestParam(value = "src", required = false) String src) {
        CompilationUnit unit = null;
        if (StringUtils.isBlank(className) || StringUtils.isBlank(src)) {
            ServiceResponseDto unpocessEntityResponse = ServiceResponseUtils.buildUnpocessableEntityResponse(className, src);
            return ResponseEntity.unprocessableEntity().body(unpocessEntityResponse);
        }
        try {
            className = URLDecoder.decode(className, StandardCharsets.UTF_8.name());
            src = URLDecoder.decode(src, StandardCharsets.UTF_8.name());
            LOG.info("Compiling unit. Class name: {}, source code: {}", className, src);
            unit = new CharSeqCompilationUnit(className, src);
            CompilationResult result = compilationService.compileUnit(unit);
            if (result.getCompStatus().getStatusCode() > 0) {
                //Sending compilation result data
                ServiceResponseDto compOkResponse = ServiceResponseUtils.buildCompServiceResponse(result);
                return ResponseEntity.ok(compOkResponse);
            } else {
                ServiceResponseDto compErrorResponse = ServiceResponseUtils.buildCompServiceResponse(result);
                //Sending compilation error info
                return ResponseEntity.ok(compErrorResponse);
            }
        } catch (CompilationServiceException e) {
            LOG.debug("Internal service error occurred while compiling unit with the class name - {}, src - {}", className, src, e);
            ServiceResponseDto errorResponseDto = ServiceResponseUtils.buildInternalServiceErrorResponse(e, unit);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponseDto);
        } catch (Exception e) {
            LOG.debug("Unexpected internal service error occurred while compiling unit with the class name - {}, src - {}", className, src, e);
            ServiceResponseDto errorResponseDto = ServiceResponseUtils.buildInternalServiceErrorResponse(e, unit);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponseDto);
        }
    }

    @GetMapping(value = "/ping")
    public String ping() {
        return COMPILER_SERVICE_OK_MESSAGE;
    }
}
