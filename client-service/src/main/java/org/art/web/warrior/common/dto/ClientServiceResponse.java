package org.art.web.warrior.common.dto;

import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ClientServiceResponse {

    private String className;
    private String src;
    private String message;
}
