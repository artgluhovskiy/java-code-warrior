package org.art.web.warrior.compiler.controller;

import org.art.web.warrior.commons.compiler.dto.CompilationRequest;
import org.art.web.warrior.commons.compiler.dto.CompilationResponse;
import org.art.web.warrior.compiler.domain.CompilationResult;
import org.art.web.warrior.compiler.domain.CompilationUnit;
import org.art.web.warrior.compiler.exception.CompilationServiceException;
import org.art.web.warrior.compiler.service.api.CompilationService;
import org.art.web.warrior.compiler.util.ServiceResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CompilationResponse compile(@Valid @RequestBody CompilationRequest requestData) {
        LOG.debug("Making the compilation request. Compilation request data: {}", requestData);
        List<CompilationUnit> requestUnits = requestData.getCompUnits().stream()
                .map(unitDto -> new CompilationUnit(unitDto.getClassName(), unitDto.getSrcCode()))
                .collect(toList());
        return submitCompilationRequest(requestUnits);
    }

    @GetMapping(value = "/ping")
    public String ping() {
        return COMPILER_SERVICE_OK_MESSAGE;
    }

    private CompilationResponse submitCompilationRequest(List<CompilationUnit> units) {
        try {
            CompilationResult result = compilationService.compileUnits(units);
            return ServiceResponseUtil.buildCompilationResponse(result);
        } catch (CompilationServiceException e) {
            LOG.info("Internal service error occurred while compiling units: {}", units, e);
            return ServiceResponseUtil.buildServiceErrorResponse(e, units);
        } catch (Exception e) {
            LOG.info("Unexpected internal service error occurred while compiling units: {}", units, e);
            return ServiceResponseUtil.buildServiceErrorResponse(e, units);
        }
    }
}
