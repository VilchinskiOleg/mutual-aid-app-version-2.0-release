package org.tms.profile_service_soap.endpoint;

import static org.tms.profile_service_soap.utils.Constant.NAMESPACE_URI;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.tms.profile_service_soap.endpoint.model.CreateProfileRequest;
import org.tms.profile_service_soap.endpoint.model.GetProfileRequest;
import org.tms.profile_service_soap.endpoint.model.ProfileResponse;
import javax.annotation.Resource;

@Endpoint
public class ProfileSoapEndpointService {

//    @Resource
//    private ProfileClientService profileClientService;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "CreateProfileRequest")
    @ResponsePayload
    public ProfileResponse createProfile(@RequestPayload CreateProfileRequest request) {
        return null;
    }


    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetProfileRequest")
    @ResponsePayload
    public ProfileResponse getProfile(@RequestPayload GetProfileRequest request) {
        return null;
    }
}