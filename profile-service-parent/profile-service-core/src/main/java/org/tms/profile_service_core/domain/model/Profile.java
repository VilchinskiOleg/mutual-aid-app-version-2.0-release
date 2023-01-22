package org.tms.profile_service_core.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class Profile {

    private String id;

    private String profileId;
    private List<Name> names;
    private List<Contact> contacts;
    private Gender gender;
    private LocalDate birthday;
    private LocalDateTime createAt;
    private LocalDateTime modifyAt;

    public enum Gender {
        MALE,
        FEMALE
    }
}
