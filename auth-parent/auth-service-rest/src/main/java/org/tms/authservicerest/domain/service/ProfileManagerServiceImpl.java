package org.tms.authservicerest.domain.service;

import static java.util.Objects.isNull;
import static java.util.Optional.ofNullable;
import static org.tms.authservicerest.utils.Constant.Service.PASSWORD_LENGTH;

import java.util.List;
import javax.annotation.Resource;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.mapper.autoconfiguration.mapper.Mapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.tms.authservicerest.domain.model.Profile;
import org.tms.authservicerest.domain.model.profile.Ticket;
import org.tms.authservicerest.domain.model.profile.Ticket.Type;
import org.tms.authservicerest.domain.service.client.EmailSenderClientService;
import org.tms.authservicerest.domain.service.jwt.JwtHandler;
import org.tms.authservicerest.domain.service.ticket.TicketHandlerStrategy;
import org.tms.authservicerest.persistent.service.ProfileRepository;

@Slf4j
@Component
public class ProfileManagerServiceImpl implements ProfileManagerService {

  @Resource
  private ProfileRepository profileRepository;
  @Resource
  private JwtHandler jwtHandler;
  @Resource
  private List<TicketHandlerStrategy> ticketHandlerStrategies;
  @Resource
  private IdGeneratorService idGeneratorService;
  @Resource
  private EmailSenderClientService emailSenderClientService;
  @Resource
  private Mapper mapper;

  @Override
  public Profile create(Profile profile) {
    profile.setProfileId(idGeneratorService.generate());
    if (isNull(profile.getPassword())) {
      profile.setWeekPassword(true);
      var generator = PasswordGenerator
              .builder()
              .length(PASSWORD_LENGTH)
              .upper().lower().digits().punctuation()
              .build();
      String temporaryPassword = generator.generatePassword();
      profile.setPassword(temporaryPassword);
      emailSenderClientService.sendEmailForResetPassword(temporaryPassword);
    }
    ticketHandlerStrategies.forEach(ticketStrategy -> ticketStrategy.addTicket(profile));
    return saveProfile(profile);
  }

  @Override
  public String login(Ticket ticket) {
    var profileData = profileRepository.findProfileByTicket(ticket);
    Profile profile;
    try {
      profile = mapper.map(ofNullable(profileData).orElseThrow(() ->
              new RuntimeException("Cannot find profile by login and ticket type.")),
          Profile.class);
      getTicketHandlerStrategy(ticket.getType()).verifyTicket(profile, ticket);
    } catch (RuntimeException ex) {
      log.error("Error while login by Ticket={}: {}", ticket, ex.getMessage());
      throw new BadCredentialsException(ex.getMessage());
    }
    return jwtHandler.createToken(profile);
  }

  private Profile saveProfile(Profile profile) {
    var dataProfile = mapper.map(profile, org.tms.authservicerest.persistent.model.Profile.class);
    return mapper.map(profileRepository.save(dataProfile), Profile.class);
  }

  private TicketHandlerStrategy getTicketHandlerStrategy(@NonNull Type type) {
    return ticketHandlerStrategies.stream()
                                  .filter(ticketStrategy -> ticketStrategy.getTye() == type)
                                  .findFirst()
                                  .orElseThrow(() -> new RuntimeException("Cannot found TicketHandlerStrategy by type."));
  }
}
