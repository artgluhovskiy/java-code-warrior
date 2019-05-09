package org.art.web.warrior.client.config.jmx;

import lombok.Getter;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

@Getter
@ManagedResource(
        objectName = "PD:category=MBeans,name=serviceConfigBean",
        description = "Client Service Configuration MBean"
)
@Component
public class ServiceConfigMBean {

    private boolean requestLoggingJmx;

    @ManagedOperation
    public void setRequestLoggingJmx(boolean requestLoggingJmx) {
        this.requestLoggingJmx = requestLoggingJmx;
    }
}
