package order.orderservice.domain.service;

import static java.time.LocalDateTime.now;
import static order.orderservice.domain.model.Order.Status.ACTIVE;

import order.orderservice.domain.model.Member;
import order.orderservice.domain.model.Order;
import order.orderservice.domain.model.page.Page;
import order.orderservice.domain.model.search.SearchOrderDetails;
import order.orderservice.persistent.repository.OrderRepository;
import org.mapper.autoconfiguration.mapper.Mapper;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.ArrayList;

@Component
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderRepository orderRepository;
    @Resource
    private Mapper mapper;

    @Override
    public Order createOrder(Order orderDetails) {
        var order = createNewOrder();
        mapper.map(orderDetails, order);
        var result = mapper.map(order, order.orderservice.persistent.entity.Order.class);
        return mapper.map(orderRepository.save(result), Order.class);
    }

    /**
     * Find order by id.
     * @param orderId
     * @return order by id or empty order if such order doesn't exist.
     */
    @Override
    public Order findByOrderId(String orderId) {
        var result = orderRepository.findByOrderId(orderId);
        if (result.isPresent()) {
            return mapper.map(result.get(), Order.class);
        }
        return new Order();
    }

    @Override
    public Page<Order> findByFilters(SearchOrderDetails searchOrderDetails) {
        var result = orderRepository.searchByFilters(searchOrderDetails);
        return Page.<Order>builder()
                .payload(mapper.map(result.getContent(), new ArrayList<>(), Order.class))
                .allPages(result.getTotalPages())
                .currentPage(result.getNumber())
                .sizeOfPage(result.getSize())
                .build();
    }



    private Order createNewOrder() {
        var order = new Order();
        order.setOrderId(generateId());
        order.setStatus(ACTIVE);
        order.setCreateAt(now());
        order.setOwner(getOwnerBySecurityContext());
        return order;
    }

    private String generateId() {
        //todo
        return "123456";
    }

    private Member getOwnerBySecurityContext() {return null;} // TODO: write method after creating base security module.
}
