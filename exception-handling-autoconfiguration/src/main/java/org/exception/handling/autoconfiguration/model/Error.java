package org.exception.handling.autoconfiguration.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Error {

    private String code;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Error> nestedErrors;
}
