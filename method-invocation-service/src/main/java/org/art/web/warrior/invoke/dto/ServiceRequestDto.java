package org.art.web.warrior.invoke.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRequestDto {

    private String className;

    private String targetMethodName;

    private String methodReturnType;

    private List<Pair<String, Object>> methodArgs;

    private byte[] compiledClass;
}
