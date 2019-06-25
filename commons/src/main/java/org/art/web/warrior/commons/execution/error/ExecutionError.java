package org.art.web.warrior.commons.execution.error;

import lombok.*;
import org.art.web.warrior.commons.common.CommonApiError;

import java.time.LocalDateTime;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ExecutionError extends CommonApiError {

    public ExecutionError(int respStatusCode, String respStatus, String message, String failedTestMessage, LocalDateTime dateTime) {
        super(respStatusCode, respStatus, message, dateTime);
        this.failedTestMessage = failedTestMessage;
    }

    @Getter
    @Setter
    private String failedTestMessage;
}
