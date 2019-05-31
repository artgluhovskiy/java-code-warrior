package org.art.web.warrior.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCodingTaskDto {

    private boolean solved = false;

    private String nameId;

    private String name;

    private String description;
}
