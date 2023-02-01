package org.tms.profile_service_soap.mapper;

import static java.util.Objects.nonNull;
import static javax.xml.datatype.DatatypeFactory.newInstance;

import lombok.SneakyThrows;
import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;
import org.tms.profile_service_core.domain.model.Profile;
import org.tms.profile_service_soap.endpoint.model.Contact;
import org.tms.profile_service_soap.endpoint.model.Name;
import java.util.ArrayList;

@Component
public class ProfileToSoapProfileConverter extends BaseConverter<Profile, org.tms.profile_service_soap.endpoint.model.Profile> {

    @Override
    protected org.tms.profile_service_soap.endpoint.model.Profile getDestination() {
        return new org.tms.profile_service_soap.endpoint.model.Profile();
    }

    @SneakyThrows
    @Override
    public void convert(Profile source, org.tms.profile_service_soap.endpoint.model.Profile destination) {
        destination.getNames().addAll(mapper.map(source.getNames(), new ArrayList<>(), Name.class));
        destination.getContacts().addAll(mapper.map(source.getContacts(), new ArrayList<>(), Contact.class));
        destination.setBirthday(newInstance().newXMLGregorianCalendar(source.getBirthday().toString()));
        destination.setGender(mapper.map(source.getGender()));

        destination.setCreateAt(newInstance().newXMLGregorianCalendar(source.getCreateAt().toString()));
        if (nonNull(source.getModifyAt())) {
            destination.setModifyAt(newInstance().newXMLGregorianCalendar(source.getModifyAt().toString()));
        }
        destination.setId(source.getProfileId());
    }
}