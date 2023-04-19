package org.tms.profile_service_core.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;
import static org.tms.profile_service_core.domain.model.Contact.Type.EMAIL;

@Getter
@Setter
public class Profile {

    private String id;

    private String profileId;
    private List<Name> names;
    private List<Contact> contacts;
    private Gender gender;
    private LocalDate birthday;

    // Optional property, only for transfer to the Auth Rest:
    private String password;

    private LocalDateTime createAt;
    private LocalDateTime modifyAt;


    public List<Contact> getContacts() {
        if (isNull(contacts)) {
            this.contacts = new ArrayList<>();
        }
        return contacts;
    }

    public List<Name> getNames() {
        if (isNull(names)) {
            this.names = new ArrayList<>();
        }
        return names;
    }

    public String getEmail() {
        return contacts.stream()
                .filter(contact -> EMAIL.equals(contact.getType()))
                .findFirst()
                .map(Contact::getValue)
                .orElse(null);
    }


    public enum Gender {
        MALE,
        FEMALE
    }
}