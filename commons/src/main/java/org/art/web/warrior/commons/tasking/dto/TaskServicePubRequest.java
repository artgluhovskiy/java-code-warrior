package org.art.web.warrior.commons.tasking.dto;

import lombok.*;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskServicePubRequest {

    private String nameId;

    private String name;

    private String description;

    private String methodSign;

    private byte[] runnerClassData;
}
