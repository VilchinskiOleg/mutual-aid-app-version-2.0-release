package order.orderservice.mapper.profile;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.lowerCase;

import order.orderservice.domain.model.Member;
import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;
import org.tms.mutual_aid.profile_service.client.model.Profile;

@Component
public class ClientProfileToMemberConverter extends BaseConverter<Profile, Member> {

    @Override
    public void convert(Profile source, Member destination) {
        destination.setMemberId(source.getId());
        var name = source.getNames().stream()
                                    .filter(n -> lowerCase(n.getLocale()).equals("en"))
                                    .findFirst()
                                    .orElse(null);
        if (nonNull(name)) {
            destination.setFirstName(name.getFirstName());
            destination.setLastName(name.getLastName());
            destination.setNickName(name.getFirstName() + "_" + name.getLastName());
        }
    }
}