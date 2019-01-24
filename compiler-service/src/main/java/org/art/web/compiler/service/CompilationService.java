package org.art.web.compiler.service;

import org.art.web.compiler.model.CompilationResult;
import org.art.web.compiler.model.CompilationUnit;

/**
 * Java source code compilation service API.
 * Core service interface, which provides simple API for compiling java src code units.
 */
public interface CompilationService {

    CompilationResult compileSource(CompilationUnit unit) throws Exception;
}
