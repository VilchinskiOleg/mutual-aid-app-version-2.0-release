package org.tms.authservicerest.rest.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.tms.authservicerest.domain.model.Profile.Gender;
import org.tms.authservicerest.domain.model.profile.Contact;
import org.tms.authservicerest.domain.model.profile.Name;

@Getter
@Setter
@ToString
public class Profile {

  private String Id;
  private String resourceId;

  private String gender;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate birthday;

  private List<Contact> contacts;
  private List<Name> names;

  /**
   * Only for input, if user wants to declare password on his own:
   */
  private String password;

  private LocalDateTime createAt;
  private LocalDateTime modifyAt;
}