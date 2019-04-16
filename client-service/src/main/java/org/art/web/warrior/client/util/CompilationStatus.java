package org.art.web.warrior.client.util;

/**
 * Compilation status, which represents a basic result of unit compilation by a Compilation Service.
 */
public enum CompilationStatus {

    SUCCESS("Success", 1),
    ERROR("Error", -1);

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
