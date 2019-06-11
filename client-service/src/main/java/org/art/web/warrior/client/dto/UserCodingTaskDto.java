package org.art.web.warrior.client.dto;

import lombok.Data;

@Data
public class UserCodingTaskDto {

    private boolean solved = false;

    private final String nameId;

    private final String name;

    private final String description;

    private final int rating;
}
