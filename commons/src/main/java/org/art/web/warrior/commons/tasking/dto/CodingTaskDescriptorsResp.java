package org.art.web.warrior.commons.tasking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CodingTaskDescriptorsResp {

    private String respStatus;

    private List<CodingTaskDescriptor> codingTasks = Collections.emptyList();
}
