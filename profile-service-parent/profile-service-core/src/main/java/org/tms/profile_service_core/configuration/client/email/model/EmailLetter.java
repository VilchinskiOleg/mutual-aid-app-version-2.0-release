package org.tms.profile_service_core.configuration.client.email.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class EmailLetter {

    private List<Personalization> personalizations;
    @JsonProperty("from")
    private Contact sender;
    private List<Content> content;
}