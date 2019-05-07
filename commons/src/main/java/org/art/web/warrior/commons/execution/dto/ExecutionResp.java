package org.art.web.warrior.commons.execution.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecutionResp {

    private String respStatus;

    private String message;

    private String failedTestMessage;
}
