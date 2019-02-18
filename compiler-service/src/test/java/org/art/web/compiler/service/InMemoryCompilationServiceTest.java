package org.art.web.compiler.service;

import org.apache.commons.lang3.StringUtils;
import org.art.web.compiler.exceptions.CompilationServiceException;
import org.art.web.compiler.model.CharSeqCompilationUnit;
import org.art.web.compiler.model.api.CompilationMessage;
import org.art.web.compiler.model.api.CompilationResult;
import org.art.web.compiler.model.api.CompilationStatus;
import org.art.web.compiler.model.api.CompilationUnit;
import org.art.web.compiler.service.api.CompilationService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.MULTILINE;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("InMemoryCompilationService Tests")
class InMemoryCompilationServiceTest {

    private static final String TEST_DATA_PATH = "compilation-service/code-samples.txt";

    private static final String SCANNER_TOKEN_DELIMITER = "\\$\\$\\$";

    private static final Pattern CLASS_NAME_PATTERN = Pattern.compile("^public class (TestClass[\\d]{1,3}) \\{$", MULTILINE);

    private static final Map<String, String> TEST_DATA = new HashMap<>();

    private final CompilationService compiler = new InMemoryCompilationService();

    @BeforeAll
    static void initAll() {
        InputStream inputData = InMemoryCompilationServiceTest.class.getClassLoader().getResourceAsStream(TEST_DATA_PATH);
        if (inputData == null) {
            throw new RuntimeException("Test data file was not found! File path: " + TEST_DATA_PATH);
        }
        InputStreamReader inputStreamReader = new InputStreamReader(inputData, StandardCharsets.UTF_8);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        try (Scanner scanner = new Scanner(bufferedReader)) {
            scanner.useDelimiter(SCANNER_TOKEN_DELIMITER);
            while (scanner.hasNext()) {
                String srcCodeSample = scanner.next();
                String testClassName = parseClassName(srcCodeSample);
                if (StringUtils.isNotBlank(srcCodeSample) && StringUtils.isNotBlank(testClassName)) {
                    TEST_DATA.put(testClassName, srcCodeSample);
                } else {
                    throw new RuntimeException("Cannot parse test class name for src: " + srcCodeSample);
                }
            }
        }
    }

    private static String parseClassName(String srcCode) {
        String className = StringUtils.EMPTY;
        Matcher matcher = CLASS_NAME_PATTERN.matcher(srcCode);
        if (matcher.find()) {
            className = matcher.group(1);
        }
        return className;
    }

    @Test
    @DisplayName("Compile null unit")
    void test0() {
        assertThrows(RuntimeException.class, () -> compiler.compileSource(null));
    }

    @Test
    @DisplayName("Compile unit with blank class name")
    void test1() {
        CompilationUnit unit = new CharSeqCompilationUnit(null, "class TestClass0_0{}");
        assertThrows(CompilationServiceException.class, () -> compiler.compileSource(unit));
    }

    @Test
    @DisplayName("Compile unit with blank src")
    void test2() {
        CompilationUnit unit = new CharSeqCompilationUnit("TestClass0_1", null);
        assertThrows(CompilationServiceException.class, () -> compiler.compileSource(unit));
    }

    @Test
    @DisplayName("Compile unit with blank params")
    void test3() {
        CompilationUnit unit = new CharSeqCompilationUnit(null, null);
        assertThrows(CompilationServiceException.class, () -> compiler.compileSource(unit));
    }

    @Test
    @DisplayName("Compile empty class")
    void test4() {
        String className = "TestClass0_2";
        String src = "class TestClass0_2{}";
        CompilationUnit unit = new CharSeqCompilationUnit(className, src);
        CompilationResult compilationResult = assertDoesNotThrow(() -> compiler.compileSource(unit));
        assertNotNull(compilationResult);
        assertSame(CompilationStatus.SUCCESS, compilationResult.getStatus());
        Class<?> clazz = compilationResult.getCompiledClass();
        assertNotNull(clazz);
        assertEquals("TestClass0_2", clazz.getSimpleName());
    }

    @Test
    @DisplayName("Compile unit with arbitrary text as src (no exception, ERROR status should be appeared)")
    void test5() {
        CompilationUnit unit = new CharSeqCompilationUnit("TestClass0_2", "Arbitrary text");
        CompilationResult compilationResult = assertDoesNotThrow(() -> compiler.compileSource(unit));
        assertNotNull(compilationResult);
        assertSame(CompilationStatus.ERROR, compilationResult.getStatus());

        CompilationMessage message = compilationResult.getMessage();
        assertNotNull(message);

        assertAll(
                () -> assertEquals("class, interface, or enum expected", message.getCauseMessage()),
                () -> assertSame(1L, message.getCodeLine()),
                () -> assertSame(1L, message.getColumnNumber())
        );
    }

    @Test
    @DisplayName("'TestClass0' compilation test (one method, no errors)")
    void testClass0(TestInfo testInfo) {
        CompilationUnit compilationUnit = getCompilationUnit(testInfo);
        assertNotNull(compilationUnit);

        CompilationResult compilationResult = assertDoesNotThrow(() -> compiler.compileSource(compilationUnit));
        assertNotNull(compilationResult);

        Class<?> compiledClass = compilationResult.getCompiledClass();

        assertSame(CompilationStatus.SUCCESS, compilationResult.getStatus());
        assertEquals(compilationUnit.getClassName(), compiledClass.getSimpleName());
    }

    @Test
    @DisplayName("'TestClass1' compilation test (one method, no errors)")
    void testClass1(TestInfo testInfo) {
        CompilationUnit compilationUnit = getCompilationUnit(testInfo);
        assertNotNull(compilationUnit);

        CompilationResult compilationResult = assertDoesNotThrow(() -> compiler.compileSource(compilationUnit));
        assertNotNull(compilationResult);

        Class<?> compiledClass = compilationResult.getCompiledClass();

        assertSame(CompilationStatus.SUCCESS, compilationResult.getStatus());
        assertEquals(compilationUnit.getClassName(), compiledClass.getSimpleName());
    }

    @Test
    @DisplayName("'TestClass2' compilation test (one method with imports, no errors)")
    void testClass2(TestInfo testInfo) {
        CompilationUnit compilationUnit = getCompilationUnit(testInfo);
        assertNotNull(compilationUnit);

        CompilationResult compilationResult = assertDoesNotThrow(() -> compiler.compileSource(compilationUnit));
        assertNotNull(compilationResult);

        Class<?> compiledClass = compilationResult.getCompiledClass();

        assertSame(CompilationStatus.SUCCESS, compilationResult.getStatus());
        assertEquals(compilationUnit.getClassName(), compiledClass.getSimpleName());
    }

    private CompilationUnit getCompilationUnit(TestInfo testInfo) {
        Optional<Method> testMethodName = testInfo.getTestMethod();
        assertTrue(testMethodName.isPresent());

        String testClassName = StringUtils.capitalize(testMethodName.get().getName());
        assertTrue(StringUtils.isNotBlank(testClassName));

        String srcCode = TEST_DATA.get(testClassName);
        assertTrue(StringUtils.isNotBlank(srcCode));

        return new CharSeqCompilationUnit(testClassName, srcCode);
    }
}