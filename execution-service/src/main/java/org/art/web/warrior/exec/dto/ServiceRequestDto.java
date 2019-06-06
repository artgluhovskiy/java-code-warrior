package org.art.web.warrior.exec.dto;

import lombok.*;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRequestDto {

    private String className;

    private String targetMethodName;

    private String methodReturnType;

    private List<Pair<String, Object>> methodArgs;

    private byte[] compiledClass;
}
