package messagechat.messagechatservice.domain.service.common;

import org.tms.mutual_aid.profile_service.client.model.Name;
import org.tms.mutual_aid.profile_service.client.model.Profile;

import java.util.List;

import static java.lang.Math.abs;
import static java.lang.Math.random;

public interface ProfileMockTestExtension {

    final String[] PROFILE_FIRST_NAMES = {"John", "Sarah", "Jeims", "Rik", "Jesika", "Piter"};
    final String[] PROFILE_LAST_NAMES = {"Smith", "Parker", "Malfoy", "Potter", "Stark", "Gibson"};

    default Profile generateProfile(String profileId) {
        var profile = new Profile();
        profile.setId(profileId);
        var name = new Name();
        name.setFirstName(PROFILE_FIRST_NAMES[abs(5 - (int)(random()*10))]);
        name.setLastName(PROFILE_LAST_NAMES[abs(5 - (int)(random()*10))]);
        name.setLocale("en");
        profile.setNames(List.of(name));
        return profile;
    }
}