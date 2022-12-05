package org.tms.authservicerest.rest.model.profile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Name {

    private String locale;
    private String firstName;
    private String lastName;
}