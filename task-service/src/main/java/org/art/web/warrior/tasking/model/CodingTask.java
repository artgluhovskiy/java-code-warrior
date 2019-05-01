package org.art.web.warrior.tasking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CodingTask {

    private String nameId;

    private String name;

    private String description;

    private String methodSign;

    private byte[] runnerClassData;
}
