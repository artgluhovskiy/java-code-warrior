package org.art.web.warrior.tasking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodingTaskDescriptor {

    @Indexed
    private String nameId;

    private String name;

    private String description;

    private int rating = 0;
}
