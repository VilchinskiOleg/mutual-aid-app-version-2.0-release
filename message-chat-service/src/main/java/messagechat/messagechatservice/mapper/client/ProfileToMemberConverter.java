package messagechat.messagechatservice.mapper.client;

import messagechat.messagechatservice.domain.model.Member;
import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;
import org.tms.mutual_aid.profile_service.client.model.Name;
import org.tms.mutual_aid.profile_service.client.model.Profile;

import java.util.Optional;

import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.upperCase;

@Component
public class ProfileToMemberConverter extends BaseConverter<Profile, Member> {

    @Override
    public void convert(Profile source, Member destination) {
        destination.setProfileId(source.getId());
        Optional<Name> nameWrapper = ofNullable(source.getNames()).map(names -> names.stream()
                .filter(name -> "EN".equals(upperCase(name.getLocale())))
                .findFirst().orElseGet(null));
        if (nameWrapper.isPresent()) {
            destination.setFirstName(nameWrapper.get().getFirstName());
            destination.setLastName(nameWrapper.get().getLastName());
        }
    }
}