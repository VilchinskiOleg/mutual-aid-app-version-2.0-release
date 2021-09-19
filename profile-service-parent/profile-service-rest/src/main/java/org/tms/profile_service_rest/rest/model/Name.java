package org.tms.profile_service_rest.rest.model;

import static org.tms.profile_service_rest.utils.Constant.Errors.PROFILE_NAME_LOCALE_RULE;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class Name {

    @NotBlank(message = "PROFILE_NAME_LOCALE_REQUIRED")
    @Pattern(regexp = "^[A-Z,a-z\\\\w]{2}$", message = PROFILE_NAME_LOCALE_RULE)
    private String locale;
    @NotBlank(message = "PROFILE_NAME_FIRST_NAME_REQUIRED")
    private String firstName;
    @NotBlank(message = "PROFILE_NAME_LAST_NAME_REQUIRED")
    private String lastName;
}
