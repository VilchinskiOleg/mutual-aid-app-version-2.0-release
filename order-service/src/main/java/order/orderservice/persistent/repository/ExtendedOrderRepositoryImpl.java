package order.orderservice.persistent.repository;

import static java.util.Objects.nonNull;
import static order.orderservice.util.Constant.MongoDb.*;
import static org.springframework.data.domain.PageRequest.of;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.support.PageableExecutionUtils.getPage;

import order.orderservice.domain.model.search.SearchOrderDetails;
import order.orderservice.persistent.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import javax.annotation.Resource;

public class ExtendedOrderRepositoryImpl implements ExtendedOrderRepository {

    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public Page<Order> searchByFilters(SearchOrderDetails details) {
        var pageRequest = of(details.getNumberOfPage(), details.getSizeOfPage());
        var query = buildSearchQuery(details);
        var orders = mongoTemplate.find(query.with(pageRequest), Order.class);
        return getPage(orders,
                       pageRequest,
                       () -> mongoTemplate.count(query.with(pageRequest).skip(-1).limit(-1), Order.class));
    }

    private Query buildSearchQuery(SearchOrderDetails details) {
        Query query = new Query();
        if (nonNull(details.getPrice())) {
            query.addCriteria(where(PRICE).is(details.getPrice()));
        }
        if (nonNull(details.getType())) {
            query.addCriteria(where(TYPE).is(details.getType()));
        }
        if (nonNull(details.getPriority())) {
            query.addCriteria(where(PRIORITY).is(details.getPriority()));
        }
        if (nonNull(details.getLocationCountry())) {
            query.addCriteria(where(LOCATION_COUNTRY).is(details.getLocationCountry()));
        }
        if (nonNull(details.getLocationCity())) {
            query.addCriteria(where(LOCATION_CITY).is(details.getLocationCity()));
        }
        if (nonNull(details.getLocationStreet())) {
            query.addCriteria(where(LOCATION_STREET).is(details.getLocationStreet()));
        }
        if (nonNull(details.getLocationHome())) {
            query.addCriteria(where(LOCATION_HOME).is(details.getLocationHome()));
        }
        return query;
    }
}
