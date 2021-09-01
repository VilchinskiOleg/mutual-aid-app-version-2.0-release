package org.tms.profile_service_rest.rest.model;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class Profile {

    private String id;

    private String profileId;
    private List<Name> names;
    private List<Contact> contacts;
    private String gender;
    private LocalDate birthday;
}
