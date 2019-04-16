package org.art.web.warrior.compiler.controller;

import lombok.SneakyThrows;
import org.art.web.warrior.compiler.dto.ServiceRequestDto;
import org.art.web.warrior.compiler.dto.ServiceResponseDto;
import org.art.web.warrior.compiler.exceptions.CompilationServiceException;
import org.art.web.warrior.compiler.model.CharSeqCompilationUnit;
import org.art.web.warrior.compiler.model.api.CompilationResult;
import org.art.web.warrior.compiler.model.api.CompilationUnit;
import org.art.web.warrior.compiler.service.api.CompilationService;
import org.art.web.warrior.compiler.util.ServiceResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import static org.art.web.warrior.compiler.service.ServiceCommonConstants.COMPILER_SERVICE_OK_MESSAGE;
import static org.art.web.warrior.compiler.service.ServiceCommonConstants.KRYO_CONTENT_TYPE;

@RestController
@RequestMapping(value = "/compile", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, KRYO_CONTENT_TYPE})
public class CompilerController {

    private static final Logger LOG = LoggerFactory.getLogger(CompilerController.class);

    private CompilationService compilationService;

    @Autowired
    public CompilerController(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @SneakyThrows(UnsupportedEncodingException.class)
    @RequestMapping(value = "/src/params", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<ServiceResponseDto> compile(@RequestParam(value = "classname", required = false) String className,
                                                      @RequestParam(value = "src", required = false) String src) {
        LOG.debug("Compilation request (request params): classname {}, src {}", className, src);
        ServiceRequestDto requestData = new ServiceRequestDto(className, src);
        if (!requestData.isValid()) {
            return cannotProcessRequestData(requestData);
        }
        className = URLDecoder.decode(className, StandardCharsets.UTF_8.name());
        src = URLDecoder.decode(src, StandardCharsets.UTF_8.name());
        CompilationUnit<CharSequence> unit = new CharSeqCompilationUnit(className, src);
        return compileCharSeqUnit(unit);
    }

    @RequestMapping(value = "/src/entity", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ServiceResponseDto> compile(@RequestBody ServiceRequestDto requestData) {
        LOG.debug("Compilation request (request entity): {}", requestData);
        if (!requestData.isValid()) {
            return cannotProcessRequestData(requestData);
        }
        CompilationUnit<CharSequence> unit = new CharSeqCompilationUnit(requestData.getClassName(), requestData.getSrc());
        return compileCharSeqUnit(unit);
    }

    @GetMapping(value = "/ping")
    public String ping() {
        return COMPILER_SERVICE_OK_MESSAGE;
    }

    private ResponseEntity<ServiceResponseDto> compileCharSeqUnit(CompilationUnit<CharSequence> unit) {
        String className = unit.getClassName();
        CharSequence src = unit.getSrcCode();
        LOG.debug("Compiling unit. Class name: {}, source code: {}", className, src);
        try {
            CompilationResult result = compilationService.compileUnit(unit);
            if (result.getCompStatus().getStatusCode() > 0) {
                ServiceResponseDto compOkResponse = ServiceResponseUtils.buildCompServiceResponse(result);
                return ResponseEntity.ok(compOkResponse);
            } else {
                ServiceResponseDto compErrorResponse = ServiceResponseUtils.buildCompServiceResponse(result);
                return ResponseEntity.ok(compErrorResponse);
            }
        } catch (CompilationServiceException e) {
            LOG.info("Internal service error occurred while compiling unit with the class name - {}, src - {}", unit.getClassName(), unit.getSrcCode(), e);
            ServiceResponseDto errorResponseDto = ServiceResponseUtils.buildInternalServiceErrorResponse(e, unit);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponseDto);
        } catch (Exception e) {
            LOG.info("Unexpected internal service error occurred while compiling unit with the class name - {}, src - {}", unit.getClassName(), unit.getSrcCode(), e);
            ServiceResponseDto errorResponseDto = ServiceResponseUtils.buildInternalServiceErrorResponse(e, unit);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponseDto);
        }
    }

    private ResponseEntity<ServiceResponseDto> cannotProcessRequestData(ServiceRequestDto requestData) {
        String className = requestData.getClassName();
        String src = requestData.getSrc();
        LOG.info("Cannot process request entity. Request data is not valid. Class name: {}, source code: {}", className, src);
        ServiceResponseDto unprocessedEntityResponse = ServiceResponseUtils.buildUnprocessableEntityResponse(className, src);
        return ResponseEntity.unprocessableEntity().body(unprocessedEntityResponse);
    }
}
