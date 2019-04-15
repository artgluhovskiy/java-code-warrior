package org.art.web.warrior.compiler.service.api;

import org.art.web.warrior.compiler.exceptions.CompilationServiceException;
import org.art.web.warrior.compiler.model.api.CompilationResult;
import org.art.web.warrior.compiler.model.api.CompilationUnit;

/**
 * Java source code compilation service API.
 * Core service interface, which provides simple API for compiling java source code.
 */
public interface CompilationService {

    CompilationResult compileUnit(CompilationUnit<?> unit) throws CompilationServiceException;
}
