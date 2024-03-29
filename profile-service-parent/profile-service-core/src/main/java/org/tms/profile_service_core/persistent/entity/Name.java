package org.tms.profile_service_core.persistent.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Locale;

@Getter
@Setter
public class Name {

    private Locale locale;
    private String firstName;
    private String lastName;
}
