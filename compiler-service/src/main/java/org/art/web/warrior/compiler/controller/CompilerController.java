package org.art.web.warrior.compiler.controller;

import org.art.web.warrior.commons.compiler.dto.CompilationRequest;
import org.art.web.warrior.commons.compiler.dto.CompilationResponse;
import org.art.web.warrior.compiler.domain.CompilationResult;
import org.art.web.warrior.compiler.domain.CompilationUnit;
import org.art.web.warrior.compiler.service.api.CompilationService;
import org.art.web.warrior.compiler.util.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.art.web.warrior.commons.CommonConstants.KRYO_CONTENT_TYPE;

@RestController
@RequestMapping("/compile")
public class CompilerController {

    private static final Logger LOG = LoggerFactory.getLogger(CompilerController.class);

    private final CompilationService compilationService;

    @Autowired
    public CompilerController(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = KRYO_CONTENT_TYPE)
    public CompilationResponse compile(@Valid @RequestBody CompilationRequest requestData) {
        LOG.debug("Making the compilation request. Compilation request data: {}", requestData);
        List<CompilationUnit> requestUnits = requestData.getCompUnits().stream()
                .map(unitDto -> new CompilationUnit(unitDto.getClassName(), unitDto.getSrcCode()))
                .collect(toList());
        CompilationResult result = compilationService.compileUnits(requestUnits);
        return ServiceUtil.buildCompilationResponse(result);
    }
}
