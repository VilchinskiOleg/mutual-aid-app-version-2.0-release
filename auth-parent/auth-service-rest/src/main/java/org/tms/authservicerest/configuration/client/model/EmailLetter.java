package org.tms.authservicerest.configuration.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class EmailLetter {

    private List<Personalization> personalizations;
    @JsonProperty("from")
    private Contact sender;
    private List<Content> content;
}