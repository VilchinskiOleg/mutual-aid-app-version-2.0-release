package org.tms.profile_service_core.persistent.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Document(collection = "profile-service.profile")
public class Profile {

    @Id
    private String id;

    private String profileId;
    private List<Name> names;
    private List<Contact> contacts;
    private String gender;
    private LocalDate birthday;
    private LocalDateTime createAt;
    private LocalDateTime modifyAt;
}