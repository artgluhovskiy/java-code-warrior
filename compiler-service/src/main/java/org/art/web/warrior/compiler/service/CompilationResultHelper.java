package org.art.web.warrior.compiler.service;

import org.art.web.warrior.commons.ServiceResponseStatus;
import org.art.web.warrior.compiler.domain.CompilationMessage;
import org.art.web.warrior.compiler.domain.CompilationResult;
import org.art.web.warrior.compiler.domain.CompilationUnit;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

class CompilationResultHelper {

    private CompilationResultHelper() {
    }

    static CompilationResult buildResults(boolean result,
                                          List<Diagnostic<? extends JavaFileObject>> diagnostics,
                                          List<? extends CompilationUnit> units,
                                          Map<String, byte[]> compiledClassData) {
        CompilationResult compilationResult;
        if (result) {
            compilationResult = new CompilationResult(ServiceResponseStatus.SUCCESS);
            Map<String, CompilationUnit> unitResults = units.stream()
                .map(unit -> populateCompiledClassData(unit, compiledClassData))
                .collect(toMap(CompilationUnit::getClassName, Function.identity()));
            compilationResult.setCompUnitResults(unitResults);
        } else {
            compilationResult = new CompilationResult(ServiceResponseStatus.COMPILATION_ERROR);
            if (!diagnostics.isEmpty()) {
                //Reporting the last diagnostic item
                Diagnostic diagnostic = diagnostics.get(diagnostics.size() - 1);
                compilationResult.setMessage(buildCompErrorMessage(diagnostic));
                Map<String, CompilationUnit> unitResults = units.stream()
                    .collect(toMap(CompilationUnit::getClassName, Function.identity()));
                compilationResult.setCompUnitResults(unitResults);
            }
        }
        return compilationResult;
    }

    private static CompilationUnit populateCompiledClassData(CompilationUnit unit, Map<String, byte[]> compClassData) {
        String className = unit.getClassName();
        if (compClassData != null) {
            unit.setCompiledClassBytes(compClassData.get(className));
        }
        return unit;
    }

    private static CompilationMessage buildCompErrorMessage(Diagnostic diagnostic) {
        return CompilationMessage.builder()
            .kind(diagnostic.getKind())
            .errorCode(diagnostic.getCode())
            .position(diagnostic.getPosition())
            .codeLine(diagnostic.getLineNumber())
            .columnNumber(diagnostic.getColumnNumber())
            .causeMessage(diagnostic.getMessage(Locale.US))
            .build();
    }
}
