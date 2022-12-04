package org.tms.authservicerest.configuration.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class Personalization {

    @JsonProperty("to")
    private List<Contact> recipients;
    private String subject;
}