package order.orderservice.domain.service;

import lombok.extern.slf4j.Slf4j;
import order.orderservice.domain.model.Order;
import order.orderservice.domain.model.page.Page;
import order.orderservice.domain.model.search.SearchOrderDetails;
import order.orderservice.domain.service.processor.EventManagerService;
import order.orderservice.domain.service.processor.IdGeneratorService;
import order.orderservice.domain.service.processor.ProfileService;
import order.orderservice.persistent.mongo.repository.OrderRepository;
import org.common.http.autoconfiguration.model.CommonData;
import org.exception.handling.autoconfiguration.throwable.ConflictException;
import org.mapper.autoconfiguration.mapper.Mapper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static java.time.LocalDateTime.now;
import static java.util.Collections.emptyList;
import static java.util.Objects.nonNull;
import static order.orderservice.domain.model.Order.Status.ACTIVE;
import static order.orderservice.util.Constant.Errors.ORDER_NOT_FUND;
import static order.orderservice.util.Constant.ModelMapper.CREATE;
import static order.orderservice.util.Constant.ModelMapper.UPDATE;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;
import static org.springframework.util.CollectionUtils.isEmpty;

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
        order.setOwner(profileService.retrieveMemberByIdRequired(orderDetails.getOwner().getMemberId()));
        return saveOrder(order);
    }

    @Override
    public Order updateOrder(Order orderDetails, String orderId) {
        var order = findByOrderIdRequired(orderId);
        mapper.map(orderDetails, order, UPDATE);
        if (nonNull(orderDetails.getExecutor())) {
            profileService.changeOrderExecutor(order, orderDetails.getExecutor());
        }
        order.setModifyAt(now());
        return saveOrder(order);
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
    public List<Order> findByExecutorOrCandidateIds(String memberId) {
        var result = orderRepository.searchByExecutorOrCandidateIds(memberId);
        if (isEmpty(result)) {
            log.info("Such 'worker' hasn't processing orders");
            return emptyList();
        }
        return mapper.map(result, new ArrayList<>(), Order.class);
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

            var orderData = mapper.map(order, order.orderservice.persistent.mongo.entity.Order.class);
            orderRepository.delete(orderData);
//            eventManagerService.processEvent(???); //TODO: it may be usefull later [*]

//            log.info("{} - finished!", currentThread().getName());
        }));
        return orders.size();
    }

    @Override
    public Order saveOrder(Order order) {
        var orderData = mapper.map(order, order.orderservice.persistent.mongo.entity.Order.class);
        var savedOrderData = orderRepository.save(orderData);
        return mapper.map(savedOrderData, Order.class);
    }

    @Override
    public List<Order> findByOwnerId(String memberId) {
        var result = orderRepository.searchByOwnerId(memberId);
        return mapper.map(result, new ArrayList<>(), Order.class);
    }

    private Order createNewOrder() {
        var order = new Order();
        order.setOrderId(idGeneratorService.generate());
        order.setStatus(ACTIVE);
        order.setCreateAt(now());
        return order;
    }

    private Page<Order> buildPageOrders(org.springframework.data.domain.Page<order.orderservice.persistent.mongo.entity.Order> pageOrdersDetails) {
        return Page
                .<Order>builder()
                .payload(mapper.map(pageOrdersDetails.getContent(), new ArrayList<>(), Order.class))
                .allPages(pageOrdersDetails.getTotalPages())
                .currentPage(pageOrdersDetails.getNumber())
                .sizeOfPage(pageOrdersDetails.getSize())
                .build();
    }
}