package org.art.web.warrior.tasking.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Data
@Builder
@NoArgsConstructor
@Embeddable
public class CodingTaskDescriptor {

    private String nameId;

    private String name;

    private String description;

    private int rating;
}
