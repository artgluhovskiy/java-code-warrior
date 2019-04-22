package org.art.web.warrior.compiler.service;

import org.apache.commons.lang3.StringUtils;
import org.art.web.warrior.compiler.domain.*;
import org.art.web.warrior.compiler.exception.CompilationServiceException;
import org.art.web.warrior.compiler.service.api.CompilationService;
import org.junit.jupiter.api.*;

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

import static java.util.Collections.singletonList;
import static java.util.regex.Pattern.MULTILINE;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("InMemoryCompilationService Tests")
class InMemoryCompilationServiceTest {

    private static final String TEST_DATA_PATH = "compilation-service/code-samples.txt";

    private static final String SCANNER_TOKEN_DELIMITER = "\\$\\$\\$";

    private static final Pattern CLASS_NAME_PATTERN = Pattern.compile("^public class (TestClass[\\d]{1,3}|TestClassFailed[\\d]{1,3}) \\{$", MULTILINE);

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
                if (StringUtils.isNotBlank(srcCodeSample)) {
                    String testClassName = parseClassName(srcCodeSample);
                    if (StringUtils.isNotBlank(testClassName)) {
                        TEST_DATA.put(testClassName, srcCodeSample);
                    }
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
        assertThrows(RuntimeException.class, () -> compiler.compileUnits(null));
    }

    @Test
    @DisplayName("Compile unit with a blank class name")
    void test1() {
        CompilationUnit unit = new CompilationUnit(null, "class TestClass0_0{}");
        assertThrows(CompilationServiceException.class, () -> compiler.compileUnits(singletonList(unit)));
    }

    @Test
    @DisplayName("Compile unit with a blank src")
    void test2() {
        CompilationUnit unit = new CompilationUnit("TestClass0_1", null);
        assertThrows(CompilationServiceException.class, () -> compiler.compileUnits(singletonList(unit)));
    }

    @Test
    @DisplayName("Compile unit with a blank params")
    void test3() {
        CompilationUnit unit = new CompilationUnit(null, null);
        assertThrows(CompilationServiceException.class, () -> compiler.compileUnits(singletonList(unit)));
    }

    @Test
    @DisplayName("Compile empty class")
    void test4() {
        String className = "TestClass0_2";
        String src = "class TestClass0_2{}";
        CompilationUnit unit = new CompilationUnit(className, src);

        CompilationResult result = assertDoesNotThrow(() -> compiler.compileUnits(singletonList(unit)));
        assertNotNull(result);
        assertSame(CompilationStatus.SUCCESS, result.getCompStatus());
        assertEquals(1, result.getCompUnitResults().size());
        UnitResult unitResult = result.getCompUnitResults().get(className);
        assertNotNull(unitResult);
        assertEquals(className, unitResult.getClassName());
        assertNotNull(unitResult.getCompiledClassBytes());
    }

    @Test
    @DisplayName("Compile unit with arbitrary text as src (no exception, ERROR status should be appeared)")
    void test5() {
        String className = "TestClass0_3";
        String src = "Arbitrary text";
        CompilationUnit unit = new CompilationUnit(className, src);
        assertThrows(CompilationServiceException.class, () -> compiler.compileUnits(singletonList(unit)));
    }

    @Test
    @DisplayName("'TestClass0' compilation test (one method, no errors)")
    void testClass0(TestInfo testInfo) {
        CompilationUnit unit = getCompilationUnit(testInfo);
        assertNotNull(unit);
        compileWithoutErrors(unit);
    }

    @Test
    @DisplayName("'TestClass1' compilation test (one method, no errors)")
    void testClass1(TestInfo testInfo) {
        CompilationUnit unit = getCompilationUnit(testInfo);
        assertNotNull(unit);
        compileWithoutErrors(unit);
    }

    @Test
    @DisplayName("'TestClass2' compilation test (one method with imports, no errors)")
    void testClass2(TestInfo testInfo) {
        CompilationUnit unit = getCompilationUnit(testInfo);
        assertNotNull(unit);
        compileWithoutErrors(unit);
    }

    @Test
    @DisplayName("'TestClass3' compilation test (two methods, no errors)")
    void testClass3(TestInfo testInfo) {
        CompilationUnit unit = getCompilationUnit(testInfo);
        assertNotNull(unit);
        compileWithoutErrors(unit);
    }

    @Test
    @DisplayName("'TestClass4' compilation test (inner class, no errors)")
    void testClass4(TestInfo testInfo) {
        CompilationUnit unit = getCompilationUnit(testInfo);
        assertNotNull(unit);
        compileWithoutErrors(unit);
    }

    @Test
    @DisplayName("'TestClass5' compilation test (static nested class, no errors)")
    void testClass5(TestInfo testInfo) {
        CompilationUnit unit = getCompilationUnit(testInfo);
        assertNotNull(unit);
        compileWithoutErrors(unit);
    }

    @Test
    @DisplayName("'TestClass6' compilation test (recursive method invocation, no errors)")
    void testClass6(TestInfo testInfo) {
        CompilationUnit unit = getCompilationUnit(testInfo);
        assertNotNull(unit);
        compileWithoutErrors(unit);
    }

    @Test
    @DisplayName("'TestClassFailed1' compilation test (with errors, missed return type)")
    void testClassFailed1(TestInfo testInfo) {
        String errorMessage = "return type required";
        CompilationUnit unit = getCompilationUnit(testInfo);
        assertNotNull(unit);
        compileWithErrors(unit, errorMessage);
    }

    @Test
    @DisplayName("'TestClassFailed2' compilation test (with errors, missed class import)")
    void testClassFailed2(TestInfo testInfo) {
        String errorMessage = "cannot find symbol";
        CompilationUnit unit = getCompilationUnit(testInfo);
        assertNotNull(unit);
        compileWithErrors(unit, errorMessage);
    }

    private CompilationUnit getCompilationUnit(TestInfo testInfo) {
        Optional<Method> testMethodName = testInfo.getTestMethod();
        assertTrue(testMethodName.isPresent());

        String testClassName = StringUtils.capitalize(testMethodName.get().getName());
        assertTrue(StringUtils.isNotBlank(testClassName));

        String srcCode = TEST_DATA.get(testClassName);
        assertTrue(StringUtils.isNotBlank(srcCode));

        return new CompilationUnit(testClassName, srcCode);
    }

    private void compileWithoutErrors(CompilationUnit unit) {
        CompilationResult result = assertDoesNotThrow(() -> compiler.compileUnits(singletonList(unit)));
        assertNotNull(result);

        assertSame(CompilationStatus.SUCCESS, result.getCompStatus());
        assertNull(result.getMessage());

        UnitResult unitResult = result.getCompUnitResults().get(unit.getClassName());
        assertNotNull(unitResult.getCompiledClassBytes());
        assertEquals(unit.getClassName(), unitResult.getClassName());
    }

    private void compileWithErrors(CompilationUnit unit, String errorMessage) {
        CompilationResult result = assertDoesNotThrow(() -> compiler.compileUnits(singletonList(unit)));
        assertNotNull(result);

        assertSame(CompilationStatus.ERROR, result.getCompStatus());
        UnitResult unitResult = result.getCompUnitResults().get(unit.getClassName());
        assertNull(unitResult.getCompiledClassBytes());

        CompilationMessage message = result.getMessage();
        assertNotNull(message);

        String causeMessage = message.getCauseMessage();
        assertTrue(StringUtils.isNotBlank(causeMessage));
        assertTrue(StringUtils.containsIgnoreCase(causeMessage, errorMessage));
    }
}