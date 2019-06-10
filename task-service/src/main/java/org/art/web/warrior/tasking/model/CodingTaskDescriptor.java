package org.art.web.warrior.tasking.model;

import lombok.*;

import javax.persistence.Embeddable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class CodingTaskDescriptor {

    private String nameId;

    private String name;

    private String description;

    private int rating;
}
