package org.tms.profile_service_soap.endpoint;

import static org.tms.profile_service_soap.utils.Constant.NAMESPACE_URI;

import org.mapper.autoconfiguration.mapper.Mapper;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.tms.profile_service_core.domain.model.Profile;
import org.tms.profile_service_core.domain.service.ProfileService;
import org.tms.profile_service_soap.endpoint.model.CreateProfileRequest;
import org.tms.profile_service_soap.endpoint.model.GetProfileRequest;
import org.tms.profile_service_soap.endpoint.model.ProfileResponse;
import javax.annotation.Resource;

@Endpoint
public class ProfileSoapEndpointService {

    @Resource
    private ProfileService profileService;
    @Resource
    private Mapper mapper;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "CreateProfileRequest")
    @ResponsePayload
    public ProfileResponse createProfile(@RequestPayload CreateProfileRequest request) {
        var profile = mapper.map(request.getProfile(), Profile.class);
        var createdProfile = profileService.create(profile);
        var response = new ProfileResponse();
        response.setProfile(mapper.map(createdProfile, org.tms.profile_service_soap.endpoint.model.Profile.class));
        return response;
    }


    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetProfileRequest")
    @ResponsePayload
    public ProfileResponse getProfile(@RequestPayload GetProfileRequest request) {
        var profile = profileService.findByProfileIdRequired(request.getProfileId());
        var response = new ProfileResponse();
        response.setProfile(mapper.map(profile, org.tms.profile_service_soap.endpoint.model.Profile.class));
        return response;
    }
}