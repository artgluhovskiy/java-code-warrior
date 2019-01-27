package org.art.web.compiler.service;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.FormattedMessage;
import org.art.web.compiler.exceptions.CompilationServiceException;
import org.art.web.compiler.exceptions.UnknownJavaSourceException;
import org.art.web.compiler.model.CharSeqCompilationUnit;
import org.art.web.compiler.model.CommonCompilationMessage;
import org.art.web.compiler.model.CommonCompilationResult;
import org.art.web.compiler.model.CommonMethodDescriptor;
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
 * Compilation service implementation.
 * Provides task compilation from {@link CompilationUnit} based on Java Compiler API.
 * The result of compilation is contained in {@link CommonCompilationResult}, which includes
 * status, diagnostics and compiled class.
 * Internally uses custom class loader in order to define a compiled class.
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
    public CompilationResult compileSource(CompilationUnit<?> unit) throws CompilationServiceException {
        Objects.requireNonNull(unit, "Compilation unit should not be null!");
        String className = unit.getClassName();
        LOG.debug("Compiling class from unit. Class name: {}", className);
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        StandardJavaFileManager stdFileManager = compiler.getStandardFileManager(diagnostics, null, null);
        MemoryClassFileManager fileManager = new MemoryClassFileManager(stdFileManager);
        List<JavaFileObject> compilationUnits = new ArrayList<>();
        compilationUnits.add(generateUnitFileObject(unit));
        try {
            JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, null, null, compilationUnits);
            boolean compilationResult = task.call();
            CustomByteClassLoader classLoader = new CustomByteClassLoader(retrieveByteClassFilesFromFm(fileManager));
            if (compilationResult) {
                Class<?> compiledClass = classLoader.loadClass(className);
                LOG.debug("Class with name {} was successfully compiled", className);
                return buildCompilationResult(true, diagnostics.getDiagnostics(), compiledClass);
            } else {
                LOG.info("Compilation failed. Class name: {}", className);
                return buildCompilationResult(false, diagnostics.getDiagnostics(), null);
            }
        } catch (Exception e) {
            LOG.error("Internal compilation exception! CompilationServiceException is thrown.");
            throw new CompilationServiceException("Internal compilation exception!", unit, e);
        }
    }

    private JavaFileObject generateUnitFileObject(CompilationUnit<?> unit) {
        String className = unit.getClassName();
        Object srcCode = unit.getSrcCode();
        if (srcCode instanceof CharSequence) {
            return new CharSequenceJavaFileObject(className, CharSequence.class.cast(srcCode));
        } else {
            throw new UnknownJavaSourceException("Current source type is not supported by the service!", srcCode.getClass());
        }
    }

    private CommonCompilationResult buildCompilationResult(boolean result,
                                                           List<Diagnostic<? extends JavaFileObject>> diagnostics,
                                                           Class<?> compiledClass) {
        CommonCompilationResult compilationResult;
        if (result) {
            compilationResult = new CommonCompilationResult(CompilationStatus.SUCCESS);
            compilationResult.setCompiledClass(compiledClass);
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

    private Map<String, byte[]> retrieveByteClassFilesFromFm(MemoryClassFileManager fileManager) {
        return fileManager.getClassFiles()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getBytes()));
    }

    private static final String testCase1ClassName = "Test";
    private static final String testCase1Src =
            "public class Test {\n" +
                    "    static {\n" +
                    "        System.out.println(\"Test class is loading!\");\n" +
                    "    }\n" +
                    "    public String printMessage(String msg) {\n" +
                    "        System.out.println(\"Message: \" + msg);\n" +
                    "        return msg;\n" +
                    "    }\n" +
                    "    public void sayHello() {\n" +
                    "        System.out.println(\"Hello from compiled task!\");\n" +
                    "    }\n" +
                    "}";

    public static void main(String[] args) throws Throwable {
        InMemoryCompilationService compiler = new InMemoryCompilationService();
        CompilationResult result = compiler.compileSource(new CharSeqCompilationUnit(testCase1ClassName, testCase1Src));
        System.out.println(result.getMessage());
        Class<?> loadedClass = result.getCompiledClass();
        System.out.println(loadedClass.getSimpleName());
        Object instance = loadedClass.newInstance();
        System.out.println(instance);

        MethodHandleInvocationService invoker = new MethodHandleInvocationService();
        CommonMethodDescriptor descriptor = new CommonMethodDescriptor(instance, "printMessage");
        Class<?> returnType = String.class;
        descriptor.setReturnType(returnType);
        descriptor.setArgs(Collections.singletonList(Pair.of("Hello New!", String.class)));

        System.out.println(returnType.cast(invoker.invokeMethod(descriptor)));
    }
}
