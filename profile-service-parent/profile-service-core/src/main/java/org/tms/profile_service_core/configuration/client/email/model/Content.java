package org.tms.profile_service_core.configuration.client.email.model;

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