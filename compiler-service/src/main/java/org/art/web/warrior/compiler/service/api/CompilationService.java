package org.art.web.warrior.compiler.service.api;

import org.art.web.warrior.compiler.exception.CompilationServiceException;
import org.art.web.warrior.compiler.domain.CompilationResult;
import org.art.web.warrior.compiler.domain.CompilationUnit;

import java.util.List;

/**
 * Java source code compilation service API.
 * Core service interface, which provides simple API for compiling
 * java source code, encapsulated into compilation units.
 */
public interface CompilationService {

    CompilationResult compileUnits(List<CompilationUnit> units) throws CompilationServiceException;
}
