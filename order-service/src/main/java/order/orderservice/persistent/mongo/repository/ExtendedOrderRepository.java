package order.orderservice.persistent.mongo.repository;

import order.orderservice.domain.model.search.SearchOrderDetails;
import order.orderservice.persistent.mongo.entity.Order;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ExtendedOrderRepository {

    Page<Order> searchByFilters(SearchOrderDetails searchOrderDetails);

    Page<Order> searchByPartOfTitle(String subTitle, Integer pageNumber, Integer size);

    List<Order> searchByExecutorOrCandidateIds(String memberId);
}
