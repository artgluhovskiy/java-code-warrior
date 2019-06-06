package org.art.web.warrior.commons.execution.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecutionResponse {

    private String respStatus;

    private String message;

    private String failedTestMessage;
}
