package org.art.web.warrior.client.exception;

import org.art.web.warrior.commons.common.CommonApiError;
import org.springframework.http.HttpStatus;

public class ExternalServiceInvocationException extends RuntimeException {

    private HttpStatus httpStatus;

    private CommonApiError apiError;

    public ExternalServiceInvocationException(String message, CommonApiError apiError, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
        this.apiError = apiError;
    }

    @Override
    public String getMessage() {
        String message = super.getMessage();
        message += apiError.toString();
        message += "\n Http status: " + httpStatus.value();
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public CommonApiError getApiError() {
        return apiError;
    }
}
