package order.orderservice.domain.service;

import static java.time.LocalDateTime.now;
import static java.util.Collections.emptyList;
import static order.orderservice.domain.model.Order.Status.*;
import static order.orderservice.util.Constant.Errors.CANNOT_ADD_NEW_CANDIDATE;
import static order.orderservice.util.Constant.Errors.ORDER_NOT_FUND;
import static org.springframework.util.CollectionUtils.isEmpty;

import lombok.extern.slf4j.Slf4j;
import order.orderservice.domain.model.Member;
import order.orderservice.domain.model.Order;
import order.orderservice.domain.model.page.Page;
import order.orderservice.domain.model.search.SearchOrderDetails;
import order.orderservice.persistent.repository.OrderRepository;
import org.common.http.autoconfiguration.model.CommonData;
import org.exception.handling.autoconfiguration.throwable.ConflictException;
import org.mapper.autoconfiguration.mapper.Mapper;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderRepository orderRepository;
    @Resource
    private Mapper mapper;
    @Resource
    private CommonData commonData;

    @Override
    public Order createOrder(Order orderDetails) {
        var order = createNewOrder();
        mapper.map(orderDetails, order);
        var orderData = mapper.map(order, order.orderservice.persistent.entity.Order.class);
        return mapper.map(orderRepository.save(orderData), Order.class);
    }

    @Override
    public Order findByOrderId(String orderId) {
        var orderData = orderRepository.findByOrderId(orderId);
        if (orderData.isEmpty()) {
            throw new ConflictException(ORDER_NOT_FUND);
        }
        return mapper.map(orderData.get(), Order.class);
    }

    @Override
    public Page<Order> findByFilters(SearchOrderDetails searchOrderDetails) {
        var result = orderRepository.searchByFilters(searchOrderDetails);
        return buildPageOrders(result);
    }

    @Override
    public Page<Order> findByPartOfTitle(String subTitle, Integer pageNumber, Integer size) {
        var result = orderRepository.searchByPartOfTitle(subTitle, pageNumber, size);
        return buildPageOrders(result);
    }

    @Override
    public List<Order> findByExecutorOrCandidateIds(String memberId) { //TODO: get memberId from spring security context ?
        commonData.setLocale(new Locale("ru"));
        var result = orderRepository.searchByExecutorOrCandidateIds(memberId);
        if (isEmpty(result)) {
            log.info("Such 'worker' hasn't processing orders");
            return emptyList();
        }
        return mapper.map(result, new ArrayList<>(), Order.class);
    }

    @Override
    public Order chooseOrder(String orderId, String memberId) { //TODO: get memberId from spring security context ?
        Order currentOrder = findByOrderId(orderId);
        checkAddingCandidatesOpportunity(currentOrder);
        currentOrder.addCandidate(new Member(memberId, null, null,null)); //TODO: get and extract candidate from profile-rest.
        currentOrder.setModifyAt(now());
        if (currentOrder.getStatus() == ACTIVE) {
            currentOrder.setStatus(AWAITING_APPROVAL);
        }
        var updatedOrder = orderRepository.save(mapper.map(currentOrder, order.orderservice.persistent.entity.Order.class));
        return mapper.map(updatedOrder, Order.class);
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

    private Page<Order> buildPageOrders(org.springframework.data.domain.Page<order.orderservice.persistent.entity.Order> pageOrdersDetails) {
        return Page
                .<Order>builder()
                .payload(mapper.map(pageOrdersDetails.getContent(), new ArrayList<>(), Order.class))
                .allPages(pageOrdersDetails.getTotalPages())
                .currentPage(pageOrdersDetails.getNumber())
                .sizeOfPage(pageOrdersDetails.getSize())
                .build();
    }

    private Member getOwnerBySecurityContext() {return new Member("1-1-1", "owner", "owner", "owner");} // TODO: write method after creating base security module.

    private void checkAddingCandidatesOpportunity(Order order) {
        var currentStatus = order.getStatus();
        if (currentStatus == IN_WORK || currentStatus == CLOSED) {
            throw new ConflictException(CANNOT_ADD_NEW_CANDIDATE);
        }
    }
}
