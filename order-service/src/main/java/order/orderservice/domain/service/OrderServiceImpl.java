package order.orderservice.domain.service;

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;
import static java.time.LocalDateTime.now;
import static java.util.Collections.emptyList;
import static java.util.Objects.nonNull;
import static order.orderservice.domain.model.Order.Status.*;
import static order.orderservice.util.Constant.Errors.*;
import static order.orderservice.util.Constant.ModelMapper.CREATE;
import static order.orderservice.util.Constant.ModelMapper.UPDATE;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;
import static org.springframework.util.CollectionUtils.isEmpty;

import lombok.extern.slf4j.Slf4j;
import order.orderservice.configuration.kafka.message.KafkaOrderEvent.OperationType;
import order.orderservice.domain.model.Member;
import order.orderservice.domain.model.Order;
import order.orderservice.domain.model.page.Page;
import order.orderservice.domain.model.search.SearchOrderDetails;
import order.orderservice.domain.service.processor.EventManagerService;
import order.orderservice.domain.service.processor.IdGeneratorService;
import order.orderservice.domain.service.processor.ProfileService;
import order.orderservice.persistent.repository.OrderRepository;
import org.exception.handling.autoconfiguration.throwable.ConflictException;
import org.common.http.autoconfiguration.model.CommonData;
import org.mapper.autoconfiguration.mapper.Mapper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderRepository orderRepository;
    @Resource
    private ProfileService profileService;
    @Resource
    private IdGeneratorService idGeneratorService;
    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Resource
    private EventManagerService eventManagerService;
    @Resource
    private CommonData commonData;
    @Resource
    private Mapper mapper;

    @Override
    public Order createOrder(Order orderDetails) {
        var order = createNewOrder();
        mapper.map(orderDetails, order, CREATE);
        Order savedOrder = saveOrder(order);
        eventManagerService.sendEvent(OperationType.CREATE, savedOrder);
        return savedOrder;
    }

    @Override
    public Order updateOrder(Order orderDetails, String orderId) {
        var order = findByOrderIdRequired(orderId);
        mapper.map(orderDetails, order, UPDATE);
        if (nonNull(orderDetails.getExecutor())) {
            profileService.changeOrderExecutor(order, orderDetails.getExecutor());
        }
        order.setModifyAt(now());
        Order savedOrder = saveOrder(order);
        eventManagerService.sendEvent(OperationType.UPDATE, savedOrder);
        return savedOrder;
    }

    @Override
    public Order findByOrderIdRequired(String orderId) {
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
        var result = orderRepository.searchByExecutorOrCandidateIds(memberId);
        if (isEmpty(result)) {
            log.info("Such 'worker' hasn't processing orders");
            return emptyList();
        }
        return mapper.map(result, new ArrayList<>(), Order.class);
    }

    @Override
    public Order chooseOrder(String orderId, String memberId) { //TODO: get memberId from spring security context ?
        Order currentOrder = findByOrderIdRequired(orderId);
        checkAddingCandidatesOpportunity(currentOrder);
        profileService.populateOrderExecutionCandidate(currentOrder, memberId);
        currentOrder.setModifyAt(now());
        if (currentOrder.getStatus() == ACTIVE) {
            currentOrder.setStatus(AWAITING_APPROVAL);
        }
        Order savedOrder = saveOrder(currentOrder);
        eventManagerService.sendEvent(OperationType.UPDATE, savedOrder);
        return savedOrder;
    }

    @Override
    public Order approveOrder(String orderId, String executorId) {
        Order currentOrder = findByOrderIdRequired(orderId);
        checkApprovingExecutionOpportunity(currentOrder);
        profileService.populateOrderExecutor(currentOrder, executorId);
        currentOrder.setStatus(IN_WORK);
        currentOrder.setModifyAt(now());
        Order savedOrder = saveOrder(currentOrder);
        eventManagerService.sendEvent(OperationType.UPDATE, savedOrder);
        return savedOrder;
    }

    @Override
    public Order closeOrder(String orderId) {
        Order currentOrder = findByOrderIdRequired(orderId);
        checkClosingOrderOpportunity(currentOrder);
        currentOrder.setStatus(CLOSED);
        currentOrder.setModifyAt(now());
        Order savedOrder = saveOrder(currentOrder);
        eventManagerService.sendEvent(OperationType.UPDATE, savedOrder);
        return savedOrder;
    }

    @Override
    public Integer removeOrdersAsync(List<Order> orders) {
        if (isEmpty(orders)) {
            return INTEGER_ZERO;
        }
        orders.forEach(order -> threadPoolTaskExecutor.execute(() -> {
//            log.info("{} - start!", currentThread().getName());

//            try {
//                sleep(3000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

            var orderData = mapper.map(order, order.orderservice.persistent.entity.Order.class);
            orderRepository.delete(orderData);
            eventManagerService.sendEvent(OperationType.DELETE, order);

//            log.info("{} - finished!", currentThread().getName());
        }));
        return orders.size();
    }

    private Order createNewOrder() {
        var order = new Order();
        order.setOrderId(idGeneratorService.generate());
        order.setStatus(ACTIVE);
        order.setCreateAt(now());
        order.setOwner(getOwnerBySecurityContext());
        return order;
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

    private void checkApprovingExecutionOpportunity(Order order) {
        var currentStatus = order.getStatus();
        if (currentStatus != AWAITING_APPROVAL) {
            throw new ConflictException(CANNOT_APPROVE_EXECUTION);
        }
    }

    private void checkClosingOrderOpportunity(Order order) {
        var currentStatus = order.getStatus();
        if (currentStatus != IN_WORK) {
            throw new ConflictException(CANNOT_CLOSE_ORDER);
        }
    }

    private Order saveOrder(Order order) {
        var orderData = mapper.map(order, order.orderservice.persistent.entity.Order.class);
        var savedOrderData = orderRepository.save(orderData);
        return mapper.map(savedOrderData, Order.class);
    }
}