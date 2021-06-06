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
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
        if (nonNull(details.getPriceFrom()) || nonNull(details.getPriceTo())) {
            query.addCriteria(
                    new Criteria()
                            .andOperator(createPriceCriterias(details.getPriceFrom(), details.getPriceTo()).toArray(new Criteria[]{}))
            );
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

    private List<Criteria> createPriceCriterias(BigDecimal priceFrom, BigDecimal priceTo) {
        List<Criteria> criterias = new ArrayList<>();
        if (nonNull(priceFrom)) {
            criterias.add(where(PRICE).gte(priceFrom));
        }
        if (nonNull(priceTo)) {
            criterias.add(where(PRICE).lte(priceTo));
        }
        return criterias;
    }
}
