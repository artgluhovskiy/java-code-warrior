package org.art.web.warrior.client.model;

import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CompErrorDetails {

    private long errorCodeLine;

    private long errorColumnNumber;

    private long errorPosition;

    private String compilerMessage;

    private String compilerErrorCode;
}
