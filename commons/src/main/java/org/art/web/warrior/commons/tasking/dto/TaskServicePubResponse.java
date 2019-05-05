package org.art.web.warrior.commons.tasking.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskServicePubResponse {

    private Long taskId;

    private String respStatus;

    private String message;
}
