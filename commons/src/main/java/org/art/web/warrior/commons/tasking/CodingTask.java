package org.art.web.warrior.commons.tasking;

import lombok.*;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodingTask {

    private String nameId;

    private String name;

    private String description;

    private String methodSign;

    private byte[] runnerClassData;
}
