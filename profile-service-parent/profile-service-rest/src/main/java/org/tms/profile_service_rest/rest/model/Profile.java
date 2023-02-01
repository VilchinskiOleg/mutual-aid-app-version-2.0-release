package org.tms.profile_service_rest.rest.model;

import static org.tms.profile_service_rest.rest.validation.annotation.ValidDate.Type.PAST;
import static org.tms.profile_service_rest.utils.Constant.Errors.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.tms.profile_service_rest.rest.validation.annotation.ValidContacts;
import org.tms.profile_service_rest.rest.validation.annotation.ValidDate;
import org.tms.profile_service_rest.rest.validation.annotation.ValidNames;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class Profile {

    private String id;
    @Valid
    @ValidNames(message = PROFILE_NAMES_RULE)
    private List<Name> names;
    @Valid
    @ValidContacts(message = PROFILE_CONTACTS_RULE)
    private List<Contact> contacts;
    @NotBlank(message = PROFILE_GENDER_REQUIRED)
    @Pattern(regexp = "^(MALE|male|FEMALE|female)$", message = PROFILE_GENDER_RULE)
    private String gender;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @ValidDate(type = PAST, message = PROFILE_BIRTHDAY_RULE)
    private LocalDate birthday;

    // Optional property.
    // If it won't be provided, auth service will send email to user with default value
    // and link for generation new password:
    //TODO: Add regexp expression to check password to be strong.
    private String password;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifyAt;
}