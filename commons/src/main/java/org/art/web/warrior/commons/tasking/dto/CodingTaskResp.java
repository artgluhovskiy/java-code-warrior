package org.art.web.warrior.commons.tasking.dto;

import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CodingTaskResp {

    private String respStatus;

    private String nameId;

    private String name;

    private String description;

    private String methodSign;

    private byte[] runnerClassData;
}
