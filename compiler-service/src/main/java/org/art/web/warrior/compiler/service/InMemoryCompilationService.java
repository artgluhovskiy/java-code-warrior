package org.art.web.warrior.compiler.service;

import lombok.extern.slf4j.Slf4j;
import org.art.web.warrior.compiler.domain.CompilationResult;
import org.art.web.warrior.compiler.domain.CompilationUnit;
import org.art.web.warrior.compiler.exception.CompilationServiceException;
import org.art.web.warrior.compiler.service.api.CompilationService;
import org.springframework.stereotype.Service;

import javax.lang.model.SourceVersion;
import javax.tools.*;
import java.util.*;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.art.web.warrior.compiler.ServiceCommonConstants.*;

/**
 * Compilation service implementation, based on a Java Compiler API.
 * Provides compilation of {@link CompilationUnit}, which represents
 * a service task, containing java source code and corresponding class name.
 * The result of compilation is contained in {@link CompilationResult},
 * which includes status, some diagnostic info and compiled class itself.
 * Internally uses custom class loader for every compilation task
 * in order to define a compiled class.
 */
@Slf4j
@Service
public class InMemoryCompilationService implements CompilationService {

    private final JavaCompiler compiler;

    public InMemoryCompilationService() {
        log.info("Compilation service initialization...");
        this.compiler = ToolProvider.getSystemJavaCompiler();
        if (log.isInfoEnabled()) {
            Set<SourceVersion> supportedVersions = compiler.getSourceVersions();
            log.info("Supported Java source code versions: {}", supportedVersions);
        }
    }

    @Override
    public CompilationResult compileUnits(List<CompilationUnit> units) {
        Objects.requireNonNull(units, COMP_UNITS_ARG_SHOULD_NOT_BE_NULL_MESSAGE);
        validateCompilationUnits(units);
        log.debug("Compiling units: {}", units);
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        StandardJavaFileManager stdFileManager = compiler.getStandardFileManager(diagnostics, null, null);
        CustomClassFileManager fileManager = new CustomClassFileManager(stdFileManager);
        List<JavaFileObject> compilationUnits = new ArrayList<>(generateSourceFileObjects(units));
        List<String> cpOptions = CompilationServiceHelper.buildCompilerOptions();
        try {
            JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, cpOptions, null, compilationUnits);
            boolean compResult = task.call();
            if (compResult) {
                log.debug("Compilation units were successfully compiled!");
                return CompilationResultHelper.buildResults(true, diagnostics.getDiagnostics(), units, retrieveClassBinData(fileManager));
            } else {
                log.warn("Compilation failed! Units: {}", units);
                return CompilationResultHelper.buildResults(false, diagnostics.getDiagnostics(), units, null);
            }
        } catch (Exception e) {
            throw new CompilationServiceException(UNEXPECTED_INTERNAL_ERROR_MESSAGE, units, e);
        }
    }

    private void validateCompilationUnits(List<? extends CompilationUnit> units) {
        List<CompilationUnit> notValidUnits = new ArrayList<>();
        for (CompilationUnit unit : units) {
            if (!unit.isValid()) {
                notValidUnits.add(unit);
            }
        }
        if (!notValidUnits.isEmpty()) {
            throw new CompilationServiceException(NOT_VALID_COMPILATION_UNITS_MESSAGE, notValidUnits);
        }
    }

    private List<JavaFileObject> generateSourceFileObjects(List<CompilationUnit> units) {
        return units.stream()
            .map(unit -> {
                String className = unit.getClassName();
                CharSequence srcCode = unit.getSrcCode();
                return new CustomJavaSourceFileObject(className, srcCode);
            }).collect(toList());
    }

    private Map<String, byte[]> retrieveClassBinData(CustomClassFileManager fileManager) {
        return fileManager.getClassFiles()
            .entrySet()
            .stream()
            .collect(toMap(Map.Entry::getKey, entry -> entry.getValue().getBytes()));
    }
}
