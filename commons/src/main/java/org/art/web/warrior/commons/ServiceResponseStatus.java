package org.art.web.warrior.commons;

public enum ServiceResponseStatus {

    SUCCESS("success", 1),
    BAD_REQUEST("bad_request", -1),
    COMPILATION_ERROR("comp_error", -2),
    INTERNAL_SERVICE_ERROR("internal_error", -3),
    TASK_PUBLICATION_ERROR("task_pub_error", -4),
    NOT_FOUND("not_found", -5),

    CODE_EXEC_ERROR("exec_error", -6),
    CODE_EXEC_INTERNAL_ERROR("exec_internal_error", -7);

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
