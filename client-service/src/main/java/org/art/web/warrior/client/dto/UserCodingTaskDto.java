package org.art.web.warrior.client.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserCodingTaskDto {

    private boolean solved = false;

    private final String nameId;

    private final String name;

    private final String description;
}
