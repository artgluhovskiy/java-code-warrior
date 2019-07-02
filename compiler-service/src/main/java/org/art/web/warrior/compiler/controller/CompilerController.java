package org.art.web.warrior.compiler.controller;

import lombok.extern.slf4j.Slf4j;
import org.art.web.warrior.commons.compiler.dto.CompilationRequest;
import org.art.web.warrior.commons.compiler.dto.CompilationResponse;
import org.art.web.warrior.compiler.domain.CompilationResult;
import org.art.web.warrior.compiler.domain.CompilationUnit;
import org.art.web.warrior.compiler.service.api.CompilationService;
import org.art.web.warrior.compiler.util.ServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.art.web.warrior.commons.CommonConstants.*;

@Slf4j
@RestController
@RequestMapping("/compile")
public class CompilerController {

    private final Environment env;

    private final CompilationService compilationService;

    @Autowired
    public CompilerController(Environment env, CompilationService compilationService) {
        this.env = env;
        this.compilationService = compilationService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = KRYO_CONTENT_TYPE)
    public CompilationResponse compile(@Valid @RequestBody CompilationRequest requestData) {
        log.debug("Making the compilation request. Compilation request data: {}", requestData);
        List<CompilationUnit> requestUnits = requestData.getCompUnits().stream()
                .map(unitDto -> new CompilationUnit(unitDto.getClassName(), unitDto.getSrcCode()))
                .collect(toList());
        CompilationResult result = compilationService.compileUnits(requestUnits);
        return ServiceUtil.buildCompilationResponse(result);
    }

    @ResponseBody
    @GetMapping
    public CompilationResponse getServiceInfo() {
        log.debug("Making the info request");
        String localPort = env.getProperty(SPRING_LOCAL_PORT_PROP_NAME);
        String appName = env.getProperty(SPRING_APPLICATION_NAME_PROP_NAME);
        String appInfo = env.getProperty(SPRING_INFO_APP_PROP_NAME);
        return ServiceUtil.buildExecutionServiceInfoResp(localPort, appName, appInfo);
    }
}
