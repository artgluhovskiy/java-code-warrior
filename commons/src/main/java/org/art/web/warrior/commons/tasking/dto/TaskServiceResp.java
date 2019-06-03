package org.art.web.warrior.commons.tasking.dto;

import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TaskServiceResp {

    private String respStatus;

    private String message;

    private CodingTaskDto task;
}
