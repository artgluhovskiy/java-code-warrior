package org.art.web.warrior.commons.compiler;

public enum ServiceResponseStatus {

    SUCCESS("success", 1),
    BAD_REQUEST("bad_request", -1),
    COMPILATION_ERROR("comp_error", -2),
    INTERNAL_SERVICE_ERROR("internal_error", -3);

    private int statusCode;
    private String statusId;

    ServiceResponseStatus(String statusId, int statusCode) {
        this.statusId = statusId;
        this.statusCode = statusCode;
    }

    public String getStatusId() {
        return statusId;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
