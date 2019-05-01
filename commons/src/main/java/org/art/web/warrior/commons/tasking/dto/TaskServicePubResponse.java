package org.art.web.warrior.commons.tasking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TaskServicePubResponse {

    private String respStatus;

    private String message;

    private Long taskId;
}
