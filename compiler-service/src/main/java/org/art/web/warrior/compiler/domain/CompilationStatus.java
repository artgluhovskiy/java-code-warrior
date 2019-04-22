package org.art.web.warrior.compiler.domain;

/**
 * Compilation status, which represents a basic result of unit compilation by a Compilation Service.
 */
public enum CompilationStatus {

    SUCCESS("success", 1),
    ERROR("comp_error", -1);

    private int statusCode;
    private String status;

    CompilationStatus(String status, int statusCode) {
        this.status = status;
        this.statusCode = statusCode;
    }

    public String getStatus() {
        return status;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
