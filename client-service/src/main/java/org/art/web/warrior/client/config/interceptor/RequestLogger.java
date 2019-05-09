package org.art.web.warrior.client.config.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.art.web.warrior.client.config.jmx.ServiceConfigMBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

@Slf4j
public class RequestLogger implements ClientHttpRequestInterceptor {

    @Autowired
    private ServiceConfigMBean configMBean;

    @Value("${client.request.logging}")
    private boolean loggingEnabled;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        if (this.loggingEnabled || (this.configMBean != null && this.configMBean.isRequestLoggingJmx())) {
            log.info("Request logging is enabled.");
            log.info("Http request URI: {}", request.getURI());
            log.info("Http request headers: {}", request.getHeaders());
        }
        return execution.execute(request, body);
    }
}
