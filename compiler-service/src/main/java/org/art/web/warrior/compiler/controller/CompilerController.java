package org.art.web.warrior.compiler.controller;

import org.art.web.warrior.commons.compiler.dto.CompServiceResponse;
import org.art.web.warrior.commons.compiler.dto.CompServiceUnitRequest;
import org.art.web.warrior.compiler.domain.CompilationResult;
import org.art.web.warrior.compiler.domain.CompilationUnit;
import org.art.web.warrior.compiler.exception.CompilationServiceException;
import org.art.web.warrior.compiler.service.api.CompilationService;
import org.art.web.warrior.compiler.util.ServiceResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.art.web.warrior.commons.CommonConstants.KRYO_CONTENT_TYPE;
import static org.art.web.warrior.compiler.ServiceCommonConstants.COMPILER_SERVICE_OK_MESSAGE;

@RestController
@RequestMapping(value = "/compile", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, KRYO_CONTENT_TYPE})
public class CompilerController {

    private static final Logger LOG = LoggerFactory.getLogger(CompilerController.class);

    private CompilationService compilationService;

    @Autowired
    public CompilerController(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @PostMapping(value = "/src", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CompServiceResponse> compile(@RequestBody List<CompServiceUnitRequest> requestData) {
        LOG.debug("Compilation request. Client request data: {}", requestData);
        for (CompServiceUnitRequest compUnit : requestData) {
            if (!compUnit.isValid()) {
                return cannotProcessRequestData(compUnit);
            }
        }
        List<CompilationUnit> requestUnits = requestData.stream()
                .map(reqData -> new CompilationUnit(reqData.getClassName(), reqData.getSrcCode()))
                .collect(toList());
        return submitCompilationRequest(requestUnits);
    }

    @GetMapping(value = "/ping")
    public String ping() {
        return COMPILER_SERVICE_OK_MESSAGE;
    }

    private ResponseEntity<CompServiceResponse> submitCompilationRequest(List<CompilationUnit> units) {
        try {
            CompilationResult result = compilationService.compileUnits(units);
            if (result.getCompStatus().getStatusCode() > 0) {
                CompServiceResponse compOkResponse = ServiceResponseUtils.buildClientResponse(result);
                return ResponseEntity.ok(compOkResponse);
            } else {
                CompServiceResponse compErrorResponse = ServiceResponseUtils.buildClientResponse(result);
                return ResponseEntity.ok(compErrorResponse);
            }
        } catch (CompilationServiceException e) {
            LOG.info("Internal service error occurred while compiling units: {}", units, e);
            CompServiceResponse errorResponseDto = ServiceResponseUtils.buildInternalServiceErrorResponse(e, units);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponseDto);
        } catch (Exception e) {
            LOG.info("Unexpected internal service error occurred while compiling units: {}", units, e);
            CompServiceResponse errorResponseDto = ServiceResponseUtils.buildInternalServiceErrorResponse(e, units);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponseDto);
        }
    }

    private ResponseEntity<CompServiceResponse> cannotProcessRequestData(CompServiceUnitRequest requestData) {
        String className = requestData.getClassName();
        String src = requestData.getSrcCode();
        LOG.info("Cannot process request entity. Request data is not valid. Class name: {}, source code: {}", className, src);
        CompServiceResponse unprocessedEntityResponse = ServiceResponseUtils.buildUnprocessableEntityResponse(className, src);
        return ResponseEntity.unprocessableEntity().body(unprocessedEntityResponse);
    }
}
