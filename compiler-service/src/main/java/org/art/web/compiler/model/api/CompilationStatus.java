package org.art.web.compiler.model.api;

import org.art.web.compiler.service.api.CompilationService;

/**
 * Compilation status, which represents a basic result of unit compilation by {@link CompilationService}.
 */
public enum CompilationStatus {

    SUCCESS("Success"),
    ERROR("Error");

    private String status;

    CompilationStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
