package org.tms.authservicerest.domain.model;

import static java.util.Objects.isNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.Nullable;
import org.tms.authservicerest.domain.model.profile.Contact;
import org.tms.authservicerest.domain.model.profile.Name;
import org.tms.authservicerest.domain.model.profile.Ticket;
import org.tms.authservicerest.domain.model.profile.Ticket.Type;

@Getter
@Setter
@ToString
public class Profile {

  private String profileId;

  private String resourceId;
  private List<Name> names;
  private List<Contact> contacts;
  private Gender gender;
  private LocalDate birthday;

  /**
   * Exists only on Domain and Persist layers. In order to process user's login:
   */
  private Set<Ticket> tickets;

  private String password;
  private boolean weekPassword;

  private LocalDateTime createAt;
  private LocalDateTime modifyAt;

  public enum Gender{
    MALE,
    FEMALE
  }

  /**
   * Lazy getter.
   * @return tickets.
   */
  public Set<Ticket> getTickets() {
    if (isNull(tickets)) {
      tickets = new HashSet<>();
    }
    return tickets;
  }

  /**
   * Lazy getter.
   * @return names.
   */
  public List<Name> getNames() {
    if (isNull(names)) {
      names = new ArrayList<>();
    }
    return names;
  }

  /**
   * Lazy getter.
   * @return contacts.
   */
  public List<Contact> getContacts() {
    if (isNull(contacts)) {
      contacts = new ArrayList<>();
    }
    return contacts;
  }

  @Nullable
  public Ticket findTicketByType(@NonNull Type type) {
    return getTickets().stream()
                       .filter(ticket -> type == ticket.getType())
                       .findFirst()
                       .orElse(null);
  }

  @Nullable
  public Contact findContactByType(@NonNull String contactType) {
    return getContacts().stream()
        .filter(contact -> contactType.equals(contact.getType().name()))
        .findFirst()
        .orElse(null);
  }

  @Nullable
  public Name getEnName() {
    return getNames().stream()
        .filter(name -> "en".equals(name.getLocale().getLanguage()))
        .findFirst()
        .orElse(null);
  }

  public void addTicket(@NonNull Ticket ticket) {
    getTickets().add(ticket);
  }
}