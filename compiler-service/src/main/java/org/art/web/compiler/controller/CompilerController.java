package org.art.web.compiler.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.art.web.compiler.dto.ServiceResponseDto;
import org.art.web.compiler.exceptions.CompilationServiceException;
import org.art.web.compiler.model.CharSeqCompilationUnit;
import org.art.web.compiler.model.api.CompilationResult;
import org.art.web.compiler.model.api.CompilationUnit;
import org.art.web.compiler.service.api.CompilationService;
import org.art.web.compiler.util.ServiceResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/compile")
public class CompilerController {

    private static final Logger LOG = LogManager.getLogger(CompilerController.class);

    private CompilationService compilationService;

    @Autowired
    public CompilerController(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @RequestMapping(value = "/src", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<ServiceResponseDto> compile(@RequestParam("src") String src,
                                                      @RequestParam("classname") String className) {
        CompilationUnit unit = null;
        LOG.info("Compiling unit. Class name: {}, source code: {}", className, src);
        try {
            unit = new CharSeqCompilationUnit(className, src);
            CompilationResult result = compilationService.compileUnit(unit);
            ServiceResponseDto serviceResponseDto = ServiceResponseUtils.buildServiceResponse(result);
            return ResponseEntity.ok(serviceResponseDto);
        } catch (CompilationServiceException e) {
            LOG.debug("Internal service error occurred while compiling unit with the class name - {}, src - {}", className, src, e);
            ServiceResponseDto errorResponseDto = ServiceResponseUtils.buildInternalServiceErrorResponse(e, unit);
            return ResponseEntity.ok(errorResponseDto);
        }
    }

}
