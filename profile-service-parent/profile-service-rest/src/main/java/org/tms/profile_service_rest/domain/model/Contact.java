package org.tms.profile_service_rest.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Contact {

    private Type type;
    private String value;

    public enum Type {
        EMAIL,
        PHONE
    }
}
