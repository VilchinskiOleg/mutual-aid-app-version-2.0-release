package order.orderservice.domain.service.scheduling;

import static java.time.LocalDateTime.now;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ONE;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;
import static org.springframework.data.domain.PageRequest.of;
import static org.springframework.util.CollectionUtils.isEmpty;

import lombok.extern.slf4j.Slf4j;
import order.orderservice.domain.service.OrderService;
import order.orderservice.persistent.mongo.entity.Order;
import order.orderservice.persistent.mongo.repository.OrderRepository;
import org.mapper.autoconfiguration.mapper.Mapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Slf4j
@Component
public class RemoveClosedOrdersJob {

    @Resource
    private OrderRepository orderRepository;
    @Resource
    private OrderService orderService;
    @Resource
    private Mapper mapper;
    @Value("${scheduler.remove-closed-orders-job.closed-order-live-hours}")
    private Integer closedOrderLiveHours;
    @Value("${scheduler.remove-closed-orders-job.page-size}")
    private Integer pageSize;

    @Scheduled(cron = "${scheduler.remove-closed-orders-job.cron}")
    public void execute() {
        LocalDateTime modifyAt = retrieveSearchModifyAtTime();
        int pageNumber = INTEGER_ZERO;
        Page<Order> closedOrdersPage;
        do {
            closedOrdersPage = orderRepository.findClosedOrders(modifyAt, of(pageNumber, pageSize));
            processPage(closedOrdersPage);
        } while (++pageNumber < closedOrdersPage.getTotalPages() - INTEGER_ONE);
    }

    private LocalDateTime retrieveSearchModifyAtTime() {
        return now().minusHours(closedOrderLiveHours);
    }

    private void processPage(Page<Order> closedOrdersPage) {
        if (isEmpty(closedOrdersPage.getContent())) {
            return;
        }
        var closedOrders = mapper.map(closedOrdersPage.getContent(), new ArrayList<>(), order.orderservice.domain.model.Order.class);
        Integer amountRemovedOrders = orderService.removeOrdersAsync(closedOrders);
        log.info("{}: remove {} orders from {}", this.getClass().getSimpleName(), amountRemovedOrders, closedOrdersPage.getContent().size());
    }
}