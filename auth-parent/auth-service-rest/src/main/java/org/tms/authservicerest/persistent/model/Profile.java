package org.tms.authservicerest.persistent.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.tms.authservicerest.persistent.model.profile.Ticket;

@Getter
@Setter
@ToString
@Document(collection = "auth-service.profile")
public class Profile {

  @Id
  private String id;

  private String profileId;
  private String resourceId;

  private String gender;
  private LocalDate birthday;
  private boolean weekPassword;

  // no return for user; only for login processing:
  private Set<Ticket> tickets;

  @CreatedDate
  private LocalDateTime createAt;
  @LastModifiedDate
  private LocalDateTime modifyAt;
}