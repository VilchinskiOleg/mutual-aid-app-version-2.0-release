package org.tms.profile_service_soap.mapper;

import static java.time.LocalDate.of;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.upperCase;
import static org.tms.profile_service_core.domain.model.Profile.Gender;

import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;
import org.tms.profile_service_core.domain.model.Contact;
import org.tms.profile_service_core.domain.model.Name;
import org.tms.profile_service_soap.endpoint.model.Profile;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.ArrayList;

@Component
public class SoapProfileToProfileConverter extends BaseConverter<Profile, org.tms.profile_service_core.domain.model.Profile> {

    @Override
    protected org.tms.profile_service_core.domain.model.Profile getDestination() {
        return new org.tms.profile_service_core.domain.model.Profile();
    }

    @Override
    public void convert(Profile source, org.tms.profile_service_core.domain.model.Profile destination) {
        destination.setNames(mapper.map(source.getNames(), new ArrayList<>(), Name.class));
        destination.setContacts(mapper.map(source.getContacts(), new ArrayList<>(), Contact.class));
        destination.setGender(mapper.map(upperCase(source.getGender()), Gender.class));
        XMLGregorianCalendar birthday = source.getBirthday();
        destination.setBirthday(of(birthday.getYear(), birthday.getMonth(), birthday.getDay()));
        if (isNotBlank(source.getPassword())) {
            destination.setPassword(source.getPassword());
        }
    }
}