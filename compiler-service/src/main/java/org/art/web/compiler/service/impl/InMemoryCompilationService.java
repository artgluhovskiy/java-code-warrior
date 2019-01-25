package org.art.web.compiler.service.impl;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.FormattedMessage;
import org.art.web.compiler.model.*;
import org.art.web.compiler.service.CharSequenceJavaFileObject;
import org.art.web.compiler.service.CompilationService;
import org.art.web.compiler.service.CustomByteClassLoader;
import org.art.web.compiler.service.MemoryClassFileManager;
import org.springframework.stereotype.Service;

import javax.lang.model.SourceVersion;
import javax.tools.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InMemoryCompilationService implements CompilationService {

    private static final Logger LOG = LogManager.getLogger(InMemoryCompilationService.class);

    private JavaCompiler compiler;

    public InMemoryCompilationService() {
        LOG.info("Compilation service initialization...");
        this.compiler = ToolProvider.getSystemJavaCompiler();
        LOG.info(() -> {
            Set<SourceVersion> supportedVersions = compiler.getSourceVersions();
            return new FormattedMessage("Supported Java source versions: {}", supportedVersions);
        });
    }

    @Override
    public CompilationResult compileSource(CompilationUnit unit) throws Exception {
        Objects.requireNonNull(unit, "Compilation unit should not be null!");
        String className = unit.getClassName();
        CharSequence srcCode = unit.getSrcCode();
        LOG.debug("Compiling class from unit. Class name: {}", className);
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        StandardJavaFileManager stdFileManager = compiler.getStandardFileManager(diagnostics, null, null);
        MemoryClassFileManager fileManager = new MemoryClassFileManager(stdFileManager);
        List<JavaFileObject> compilationUnits = new ArrayList<>();
        compilationUnits.add(new CharSequenceJavaFileObject(className, srcCode));
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, null, null, compilationUnits);
        boolean compilationResult = task.call();
        Class<?> compiledClass = null;
        CustomByteClassLoader classLoader = new CustomByteClassLoader(retrieveByteClassFilesFromFm(fileManager));
        if (compilationResult) {
            compiledClass = classLoader.loadClass(className);
            LOG.debug("Class with name {} was successfully compiled", className);
            return buildCompilationResult(true, diagnostics.getDiagnostics(), compiledClass);

        }
        LOG.info("Compilation failed. Class name: {}", className);
        return buildCompilationResult(false, diagnostics.getDiagnostics(), compiledClass);
    }

    private CompilationResult buildCompilationResult(boolean result,
                                                     List<Diagnostic<? extends JavaFileObject>> diagnostics,
                                                     Class<?> compiledClass) {
        CompilationResult compilationResult;
        if (result) {
            compilationResult = new CompilationResult(CompilationStatus.SUCCESS);
            compilationResult.setCompiledClass(compiledClass);
        } else {
            compilationResult = new CompilationResult(CompilationStatus.ERROR);
            if (!diagnostics.isEmpty()) {
                //Reporting last diagnostic item
                Diagnostic diagnostic = diagnostics.get(diagnostics.size() - 1);
                CompilationMessage message = CompilationMessage
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
        CompilationResult result = compiler.compileSource(new CompilationUnit(testCase1ClassName, testCase1Src));
        System.out.println(result.getMessage());
        Class<?> loadedClass = result.getCompiledClass();
        System.out.println(loadedClass.getSimpleName());
        Object instance = loadedClass.newInstance();
        System.out.println(instance);

        MethodHandleInvocationService invoker = new MethodHandleInvocationService();
        MethodDescriptor descriptor = new MethodDescriptor(instance, "printMessage");
        Class<?> returnType = String.class;
        descriptor.setReturnType(returnType);
        descriptor.setArgs(Collections.singletonList(Pair.of("Hello New!", String.class)));

        System.out.println(returnType.cast(invoker.invokeVirtual(descriptor)));
    }
}
