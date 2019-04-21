package org.art.web.warrior.compiler.example;

import org.art.web.warrior.compiler.exceptions.CompilationServiceException;
import org.art.web.warrior.compiler.model.CharSeqCompilationUnit;
import org.art.web.warrior.compiler.model.api.CompilationResult;
import org.art.web.warrior.compiler.model.api.CompilationUnit;
import org.art.web.warrior.compiler.service.CustomByteClassLoader;
import org.art.web.warrior.compiler.service.InMemoryCompilationService;

import javax.tools.JavaFileObject;
import java.util.ArrayList;
import java.util.List;

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

    public static void main(String[] args) throws CompilationServiceException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        InMemoryCompilationService compilationService = new InMemoryCompilationService();
        CompilationUnit<?> solutionUnit = new CharSeqCompilationUnit("Solution", solutionSrcCode);
        CompilationUnit<?> runnerUnit = new CharSeqCompilationUnit("SolutionRunner", runnerSrcCode);
        List<CompilationUnit<?>> compilationUnits = new ArrayList<>();
        compilationUnits.add(solutionUnit);
        compilationUnits.add(runnerUnit);

        CompilationResult compilationResult = compilationService.compileUnit(compilationUnits);

//        CustomByteClassLoader customByteClassLoader = new CustomByteClassLoader();
//        customByteClassLoader.addClassData("Solution", compilationResult.getCompiledClassBytes());
//        Class<?> solution = customByteClassLoader.loadClass("Solution");
//        System.out.println(solution.getSimpleName());
//        System.out.println(solution.newInstance());
//        System.out.println("Class for name: " + Class.forName("Solution", true, customByteClassLoader).getName());

    }
}
