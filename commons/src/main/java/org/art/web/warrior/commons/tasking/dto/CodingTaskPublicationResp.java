package org.art.web.warrior.commons.tasking.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CodingTaskPublicationResp {

    private String respStatus;

    private String message;

    private Long taskId;
}
