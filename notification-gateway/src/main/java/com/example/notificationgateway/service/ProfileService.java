package com.example.notificationgateway.service;

import org.springframework.stereotype.Component;
import org.tms.mutual_aid.profile_service.client.model.Contact;

import java.util.List;

@Component
public class ProfileService {

    public List<Contact> getProfileContacts(String profileId) {
        return List.of(new Contact().type("EMAIL").value("vilchinskioleg@gmail.com"));
    }
}
