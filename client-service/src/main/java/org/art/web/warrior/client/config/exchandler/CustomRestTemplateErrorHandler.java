package org.art.web.warrior.client.config.exchandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.art.web.warrior.client.exception.ExternalServiceInvocationException;
import org.art.web.warrior.commons.common.CommonApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.UnknownHttpStatusCodeException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.art.web.warrior.client.CommonServiceConstants.EXTERNAL_SERVICE_ERROR_MESSAGE;

@Slf4j
public class CustomRestTemplateErrorHandler extends DefaultResponseErrorHandler {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        HttpStatus httpStatus = HttpStatus.resolve(response.getRawStatusCode());
        if (httpStatus == null) {
            throw new UnknownHttpStatusCodeException(response.getRawStatusCode(), response.getStatusText(),
                response.getHeaders(), getResponseBody(response), getCharset(response));
        }
        if (httpStatus.isError()) {
            byte[] respBodyBytes = getResponseBody(response);
            String respBodyStr = new String(respBodyBytes, StandardCharsets.UTF_8);
            CommonApiError errorDetails = OBJECT_MAPPER.readValue(respBodyStr, CommonApiError.class);
            throw new ExternalServiceInvocationException(EXTERNAL_SERVICE_ERROR_MESSAGE, errorDetails, httpStatus);
        }
    }
}
