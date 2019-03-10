package org.art.web.compiler.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.FormattedMessage;
import org.art.web.compiler.exceptions.CompilationServiceException;
import org.art.web.compiler.exceptions.UnknownJavaSourceException;
import org.art.web.compiler.model.CommonCompilationMessage;
import org.art.web.compiler.model.CommonCompilationResult;
import org.art.web.compiler.model.api.CompilationResult;
import org.art.web.compiler.model.api.CompilationStatus;
import org.art.web.compiler.model.api.CompilationUnit;
import org.art.web.compiler.service.api.CompilationService;
import org.springframework.stereotype.Service;

import javax.lang.model.SourceVersion;
import javax.tools.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Compilation service implementation, based on Java Compiler API.
 * Provides compilation of {@link CompilationUnit}, which represents
 * a service task, containing java source code and corresponding class name.
 * The result of compilation is contained in {@link CommonCompilationResult},
 * which includes status, some diagnostic info and compiled class itself.
 * Internally uses custom class loader for every compilation task
 * in order to define a compiled class.
 */
@Service
public class InMemoryCompilationService implements CompilationService {

    private static final Logger LOG = LogManager.getLogger(InMemoryCompilationService.class);

    private JavaCompiler compiler;

    public InMemoryCompilationService() {
        LOG.info("Compilation service initialization...");
        this.compiler = ToolProvider.getSystemJavaCompiler();
        LOG.info(() -> {
            Set<SourceVersion> supportedVersions = compiler.getSourceVersions();
            return new FormattedMessage("Supported Java source code versions: {}", supportedVersions);
        });
    }

    @Override
    public CompilationResult compileUnit(CompilationUnit<?> unit) throws CompilationServiceException {
        Objects.requireNonNull(unit, "Compilation unit should not be null!");
        if (!unit.isValid()) throw new CompilationServiceException("Failed to compile the unit. Compilation unit is not valid!", unit);
        String className = unit.getClassName();
        LOG.debug("Compiling the unit. Target class name: {}", className);
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        StandardJavaFileManager stdFileManager = compiler.getStandardFileManager(diagnostics, null, null);
        MemoryClassFileManager fileManager = new MemoryClassFileManager(stdFileManager);
        List<JavaFileObject> compilationUnits = new ArrayList<>();
        compilationUnits.add(generateUnitFileObject(unit));
        try {
            JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, null, null, compilationUnits);
            boolean compilationResult = task.call();
            if (compilationResult) {
                LOG.debug("Class with name {} was successfully compiled", className);
                return buildCompilationResult(true, diagnostics.getDiagnostics(), retrieveClassBinData(fileManager));
            } else {
                LOG.warn("Compilation failed. Class name: {}, source code: {}", className, unit.getSrcCode());
                return buildCompilationResult(false, diagnostics.getDiagnostics(), null);
            }
        } catch (Exception e) {
            LOG.error("Unexpected error occurred while unit compilation!");
            throw new CompilationServiceException("Unexpected error occurred while unit compilation!", unit, e);
        }
    }

    private JavaFileObject generateUnitFileObject(CompilationUnit<?> unit) {
        String className = unit.getClassName();
        Object srcCode = unit.getSrcCode();
        if (srcCode instanceof CharSequence) {
            return new CharSequenceJavaFileObject(className, (CharSequence) srcCode);
        } else {
            throw new UnknownJavaSourceException("Current source type is not supported by the service!", srcCode.getClass());
        }
    }

    private CommonCompilationResult buildCompilationResult(boolean result,
                                                           List<Diagnostic<? extends JavaFileObject>> diagnostics,
                                                           Map<String, byte[]> compiledClassData) {
        CommonCompilationResult compilationResult;
        if (result) {
            compilationResult = new CommonCompilationResult(CompilationStatus.SUCCESS);
            compilationResult.setCompiledClassData(compiledClassData);
        } else {
            compilationResult = new CommonCompilationResult(CompilationStatus.ERROR);
            if (!diagnostics.isEmpty()) {
                //Reporting last diagnostic item
                Diagnostic diagnostic = diagnostics.get(diagnostics.size() - 1);
                CommonCompilationMessage message = CommonCompilationMessage
                        .builder()
                        .kind(diagnostic.getKind())
                        .errorCode(diagnostic.getCode())
                        .position(diagnostic.getPosition())
                        .codeLine(diagnostic.getLineNumber())
                        .columnNumber(diagnostic.getColumnNumber())
                        .causeMessage(diagnostic.getMessage(Locale.US))
                        .build();
                compilationResult.setMessage(message);
            }
        }
        return compilationResult;
    }

    private Map<String, byte[]> retrieveClassBinData(MemoryClassFileManager fileManager) {
        return fileManager.getClassFiles()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getBytes()));
    }
}
