package org.tms.profile_service_rest.rest.model;

import static org.tms.profile_service_rest.utils.Constant.Errors.PROFILE_CONTACT_TYPE_RULE;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class Contact {

    @NotBlank(message = "PROFILE_CONTACT_TYPE_REQUIRED")
    @Pattern(regexp = "^(EMAIL|email|PHONE|phone)$", message = PROFILE_CONTACT_TYPE_RULE)
    private String type;
    @NotBlank(message = "PROFILE_CONTACT_VALUE_REQUIRED")
    private String value;
}
