package org.art.web.compiler.model;

/**
 * Represents compilation status of source unit.
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
