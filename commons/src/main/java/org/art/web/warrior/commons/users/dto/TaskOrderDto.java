package org.art.web.warrior.commons.users.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskOrderDto {

    @NotBlank
    private String nameId;

    @NotBlank
    private String name;

    private String description;

    private LocalDateTime regDate;
}
