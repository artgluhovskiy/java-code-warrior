package org.art.web.compiler.service.api;

import org.art.web.compiler.exceptions.CompilationServiceException;
import org.art.web.compiler.model.api.CompilationResult;
import org.art.web.compiler.model.api.CompilationUnit;

/**
 * Java source code compilation service API.
 * Core service interface, which provides simple API for compiling java source code.
 */
public interface CompilationService {

    CompilationResult compileUnit(CompilationUnit<?> unit) throws CompilationServiceException;
}
