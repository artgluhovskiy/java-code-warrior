package org.art.web.warrior.compiler.example;

import org.art.web.warrior.compiler.exception.CompilationServiceException;
import org.art.web.warrior.compiler.domain.CompilationResult;
import org.art.web.warrior.compiler.domain.CompilationUnit;
import org.art.web.warrior.compiler.domain.UnitResult;
import org.art.web.warrior.compiler.service.CustomByteClassLoader;
import org.art.web.warrior.compiler.service.InMemoryCompilationService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Test {

    private static String solutionSrcCode =
            "public class Solution {\n" +
            "    public boolean isPalindrome(int x) {\n" +
            "        int initialNum = x;\n" +
            "        int reversed = 0;\n" +
            "        if (x < 0) {\n" +
            "            return false;\n" +
            "        }\n" +
            "        while (x != 0) {\n" +
            "            reversed = reversed * 10 + x % 10;\n" +
            "            x = x / 10;\n" +
            "        }\n" +
            "        return initialNum == reversed;\n" +
            "    }\n" +
            "}";

    private static String runnerSrcCode =
            "public class SolutionRunner {\n" +
            "\n" +
            "    private Solution solution;\n" +
            "\n" +
            "    public void run() {\n" +
            "        System.out.println(\"Hello from run method!\");" +
            "        System.out.println(solution);" +
            "        test0();\n" +
            "        test1();\n" +
            "    }\n" +
            "\n" +
            "    private void test0() {\n" +
            "        boolean actual = solution.isPalindrome(121);\n" +
            "        if (!actual) {\n" +
            "            throw new RuntimeException();\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    private void test1() {\n" +
            "        boolean actual = solution.isPalindrome(-121);\n" +
            "        if (actual) {\n" +
            "            throw new RuntimeException();\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    public void setSolution(Solution solution) {\n" +
            "        this.solution = solution;\n" +
            "    }\n" +
            "}";

    public static void main(String[] args) throws CompilationServiceException, ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        InMemoryCompilationService compilationService = new InMemoryCompilationService();
        String solutionClassName = "Solution";
        String runnerClassName = "SolutionRunner";
        CompilationUnit solutionUnit = new CompilationUnit(solutionClassName, solutionSrcCode);
        CompilationUnit runnerUnit = new CompilationUnit(runnerClassName, runnerSrcCode);

        List<CompilationUnit> compilationUnits = new ArrayList<>();
        compilationUnits.add(solutionUnit);
        compilationUnits.add(runnerUnit);

        CompilationResult compilationResult = compilationService.compileUnits(compilationUnits);

        Map<String, UnitResult> compUnitResults = compilationResult.getCompUnitResults();
        byte[] solutionClassData = compUnitResults.get(solutionClassName).getCompiledClassBytes();
        byte[] solutionRunnerClassData = compUnitResults.get(runnerClassName).getCompiledClassBytes();

        CustomByteClassLoader customByteClassLoader = new CustomByteClassLoader();
        customByteClassLoader.addClassData(solutionClassName, solutionClassData);
        customByteClassLoader.addClassData(runnerClassName, solutionRunnerClassData);

        Class<?> solutionClass = customByteClassLoader.loadClass(solutionClassName);
        Class<?> solutionRunnerClass = customByteClassLoader.loadClass(runnerClassName);

        Object runnerInstance = solutionRunnerClass.newInstance();

        Method solutionSetter = solutionRunnerClass.getDeclaredMethod("setSolution", solutionClass);

        solutionSetter.invoke(runnerInstance, solutionClass.cast(solutionClass.newInstance()));

        Method runMethod = solutionRunnerClass.getDeclaredMethod("run");

        runMethod.invoke(runnerInstance);
    }
}
