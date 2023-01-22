package org.tms.profile_service_rest.rest.service;

import static org.springframework.http.HttpStatus.*;

import io.swagger.annotations.ApiOperation;
import org.common.http.autoconfiguration.annotation.Api;
import org.mapper.autoconfiguration.mapper.Mapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.tms.profile_service_core.domain.service.ProfileService;
import org.tms.profile_service_rest.rest.message.ProfileResponse;
import org.tms.profile_service_rest.rest.model.Profile;
import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping(path = "api/profile-service")
public class ProfileRest {

    @Resource
    private ProfileService profileService;
    @Resource
    private Mapper mapper;

    @Api
    @ApiOperation(value = "${profile.operation.create-profile}")
    @PostMapping
    @ResponseStatus(CREATED)
    public ProfileResponse createProfile(@RequestBody @Valid Profile profile) {
        var profileDetails = mapper.map(profile, org.tms.profile_service_core.domain.model.Profile.class);
        var result = profileService.create(profileDetails);
        return new ProfileResponse(mapper.map(result, Profile.class));
    }

    @Api
    @ApiOperation(value = "${profile.operation.get-profile}")
    @GetMapping(path = "/{profile-id}")
    @ResponseStatus(OK)
    @PreAuthorize("hasRole('GET_PROFILE_BY_ID') or #profileId == authentication.profileId")
    public ProfileResponse getProfileById(@PathVariable("profile-id") String profileId) {
        var result = profileService.findByProfileIdRequired(profileId);
        return new ProfileResponse(mapper.map(result, Profile.class));
    }
}