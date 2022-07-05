package org.tms.authservicerest.persistent.service;

import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;
import static org.springframework.data.mongodb.core.query.Criteria.where;

import javax.annotation.Resource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.tms.authservicerest.domain.model.profile.Ticket;
import org.tms.authservicerest.persistent.model.Profile;

public class ExtendedProfileRepositoryImpl implements ExtendedProfileRepository {

  private static final String TICKETS_KEY = "tickets";
  private static final String TICKET_TYPE_KEY = "type";
  private static final String TICKET_LOGIN_KEY = "login";

  @Resource
  private MongoTemplate mongoTemplate;

  @Override
  public Profile findProfileByTicket(Ticket ticket) {
    var searchQuery = new Query(new Criteria(TICKETS_KEY).elemMatch(buildTicketConditions(ticket)));
    return mongoTemplate.find(searchQuery, Profile.class).get(INTEGER_ZERO);
  }

  private Criteria buildTicketConditions(Ticket ticket) {
    Criteria[] conditions = {
        where(TICKET_TYPE_KEY).is(ticket.getType()),
        where(TICKET_LOGIN_KEY).is(ticket.getLogin())
    };
    return new Criteria().andOperator(conditions);
  }
}
