package org.art.web.warrior.tasking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class CodingTaskDescriptor {

    private String nameId;

    private String name;

    private String description;

    private String methodSign;

    private int rating;
}
