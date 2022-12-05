package org.tms.authservicerest.configuration.client.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Content {

    private String type;
    private String value;
}