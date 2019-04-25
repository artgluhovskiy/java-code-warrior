package org.art.web.warrior.compiler.service;

import org.art.web.warrior.commons.compiler.ServiceResponseStatus;
import org.art.web.warrior.commons.compiler.dto.CompServiceUnitResponse;
import org.art.web.warrior.compiler.domain.CompilationMessage;
import org.art.web.warrior.compiler.domain.CompilationResult;
import org.art.web.warrior.compiler.domain.CompilationUnit;
import org.art.web.warrior.compiler.exception.CompilationServiceException;
import org.art.web.warrior.compiler.service.api.CompilationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.lang.model.SourceVersion;
import javax.tools.*;
import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.art.web.warrior.compiler.ServiceCommonConstants.*;

/**
 * Compilation service implementation, based on Java Compiler API.
 * Provides compilation of {@link CompilationUnit}, which represents
 * a service task, containing java source code and corresponding class name.
 * The result of compilation is contained in {@link CompilationResult},
 * which includes status, some diagnostic info and compiled class itself.
 * Internally uses custom class loader for every compilation task
 * in order to define a compiled class.
 */
@Service
public class InMemoryCompilationService implements CompilationService {

    private static final Logger LOG = LoggerFactory.getLogger(InMemoryCompilationService.class);

    private final JavaCompiler compiler;

    public InMemoryCompilationService() {
        LOG.info("Compilation service initialization...");
        this.compiler = ToolProvider.getSystemJavaCompiler();
        if (LOG.isInfoEnabled()) {
            Set<SourceVersion> supportedVersions = compiler.getSourceVersions();
            LOG.info("Supported Java source code versions: {}", supportedVersions);
        }
    }

    @Override
    public CompilationResult compileUnits(List<CompilationUnit> units) throws CompilationServiceException {
        Objects.requireNonNull(units, COMP_UNITS_ARG_SHOULD_NOT_BE_NULL_MESSAGE);
        validateUnits(units);
        LOG.debug("Compiling units: {}", units);
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        StandardJavaFileManager stdFileManager = compiler.getStandardFileManager(diagnostics, null, null);
        CustomClassFileManager fileManager = new CustomClassFileManager(stdFileManager);
        List<JavaFileObject> compilationUnits = new ArrayList<>(generateSourceFileObjects(units));
        try {
            JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, null, null, compilationUnits);
            boolean compResult = task.call();
            if (compResult) {
                LOG.debug("Compilation units were successfully compiled!");
                return buildCompilationResult(true, diagnostics.getDiagnostics(), units, retrieveClassBinData(fileManager));
            } else {
                LOG.warn("Compilation failed! Units: {}", units);
                return buildCompilationResult(false, diagnostics.getDiagnostics(), units, null);
            }
        } catch (Exception e) {
            throw new CompilationServiceException(UNEXPECTED_INTERNAL_ERROR_MESSAGE, units, e);
        }
    }

    private void validateUnits(List<? extends CompilationUnit> units) throws CompilationServiceException {
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
                    return new CharSeqJavaSourceFileObject(className, srcCode);
                }).collect(toList());
    }

    private CompilationResult buildCompilationResult(boolean result,
                                                     List<Diagnostic<? extends JavaFileObject>> diagnostics,
                                                     List<? extends CompilationUnit> units,
                                                     Map<String, byte[]> compiledClassData) {
        CompilationResult compilationResult;
        if (result) {
            compilationResult = new CompilationResult(ServiceResponseStatus.SUCCESS);
            Map<String, CompServiceUnitResponse> unitResults = units.stream()
                    .map(unit -> mapToUnitResult(unit, compiledClassData))
                    .collect(toMap(CompServiceUnitResponse::getClassName, Function.identity()));
            compilationResult.setCompUnitResults(unitResults);
        } else {
            compilationResult = new CompilationResult(ServiceResponseStatus.COMPILATION_ERROR);
            if (!diagnostics.isEmpty()) {
                //Reporting the last diagnostic item
                Diagnostic diagnostic = diagnostics.get(diagnostics.size() - 1);
                compilationResult.setMessage(buildCompErrorMessage(diagnostic));
                Map<String, CompServiceUnitResponse> unitResults = units.stream()
                        .map(unit -> mapToUnitResult(unit, null))
                        .collect(toMap(CompServiceUnitResponse::getClassName, Function.identity()));
                compilationResult.setCompUnitResults(unitResults);
            }
        }
        return compilationResult;
    }

    private CompServiceUnitResponse mapToUnitResult(CompilationUnit unit, Map<String, byte[]> compClassData) {
        CompServiceUnitResponse unitResult = new CompServiceUnitResponse();
        String className = unit.getClassName();
        unitResult.setClassName(className);
        unitResult.setSrcCode(unit.getSrcCode().toString());
        if (compClassData != null) {
            unitResult.setCompiledClassBytes(compClassData.get(className));
        }
        return unitResult;
    }

    private CompilationMessage buildCompErrorMessage(Diagnostic diagnostic) {
        return CompilationMessage
                .builder()
                .kind(diagnostic.getKind())
                .errorCode(diagnostic.getCode())
                .position(diagnostic.getPosition())
                .codeLine(diagnostic.getLineNumber())
                .columnNumber(diagnostic.getColumnNumber())
                .causeMessage(diagnostic.getMessage(Locale.US))
                .build();
    }

    private Map<String, byte[]> retrieveClassBinData(CustomClassFileManager fileManager) {
        return fileManager.getClassFiles()
                .entrySet()
                .stream()
                .collect(toMap(Map.Entry::getKey, entry -> entry.getValue().getBytes()));
    }
}
