package org.art.web.compiler.controller;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.art.web.compiler.dto.ServiceResponseDto;
import org.art.web.compiler.exceptions.CompilationServiceException;
import org.art.web.compiler.model.CharSeqCompilationUnit;
import org.art.web.compiler.model.Entity;
import org.art.web.compiler.model.api.CompilationResult;
import org.art.web.compiler.model.api.CompilationUnit;
import org.art.web.compiler.service.api.CompilationService;
import org.art.web.compiler.util.ServiceResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.art.web.compiler.service.ServiceCommonConstants.COMPILER_SERVICE_OK_MESSAGE;
import static org.art.web.compiler.service.ServiceCommonConstants.KRYO_CONTENT_TYPE;

@RestController
@RequestMapping(value = "/compile", produces = {KRYO_CONTENT_TYPE})
public class CompilerController {

    private static final Logger LOG = LogManager.getLogger(CompilerController.class);

    private CompilationService compilationService;

    @Autowired
    public CompilerController(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @RequestMapping(value = "/src", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity compile(@RequestParam("src") String src,
                                  @RequestParam("classname") String className) {
        CompilationUnit unit = null;
        LOG.info("Compiling unit. Class name: {}, source code: {}", className, src);
        try {
            className = URLDecoder.decode(className, StandardCharsets.UTF_8.name());
            src = URLDecoder.decode(src, StandardCharsets.UTF_8.name());
            unit = new CharSeqCompilationUnit(className, src);
            CompilationResult result = compilationService.compileUnit(unit);
            if (result.getCompStatus().getStatusCode() < 0) {
                ServiceResponseDto compErrorResponse = ServiceResponseUtils.buildCompErrorServiceResponse(result);
                //Sending compilation error info
                return ResponseEntity.ok(compErrorResponse);
            } else {
                //Sending compiled class data
                Map<String, byte[]> compiledClassData = result.getCompiledClassData();
                byte[] compiledClass = compiledClassData.get(className);
                Kryo kryo = new Kryo();
                kryo.register(Entity.class);
                Entity entity = new Entity("ArtId", 26, "I am binary!".getBytes(StandardCharsets.UTF_8));
                return ResponseEntity.ok(entity);
            }
        } catch (CompilationServiceException e) {
            LOG.debug("Internal service error occurred while compiling unit with the class name - {}, src - {}", className, src, e);
            ServiceResponseDto errorResponseDto = ServiceResponseUtils.buildInternalServiceErrorResponse(e, unit);
            return ResponseEntity.ok(errorResponseDto);
        } catch (Exception e) {
            LOG.debug("Unexpected internal service error occurred while compiling unit with the class name - {}, src - {}", className, src, e);
            ServiceResponseDto errorResponseDto = ServiceResponseUtils.buildInternalServiceErrorResponse(e, unit);
            return ResponseEntity.ok(errorResponseDto);
        }
    }

    @GetMapping(value = "/ping")
    public String ping() {
        return COMPILER_SERVICE_OK_MESSAGE;
    }
}
