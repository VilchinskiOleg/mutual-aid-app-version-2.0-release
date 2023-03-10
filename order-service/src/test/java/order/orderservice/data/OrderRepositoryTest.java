package order.orderservice.data;

import static com.mongodb.assertions.Assertions.assertTrue;
import static java.math.BigDecimal.valueOf;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.of;
import static order.orderservice.domain.model.search.SearchOrderDetails.builder;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import order.orderservice.persistent.mongo.entity.Order;
import order.orderservice.persistent.mongo.repository.OrderRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Disabled //TODO: Only for a short wile, during Jenkins deploy testing. Drop that annotation after!
@Testcontainers
@DirtiesContext

@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
public class OrderRepositoryTest {

    @Container
    private static final MongoDBContainer mongoDB = new MongoDBContainer("mongo:5.0.12");
    private static final String RESOURCE_PREFIX_PATH = "ordermock/data/";

    @Resource
    private OrderRepository orderRepository;



    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDB::getReplicaSetUrl);
    }

    @Test
    void find_orders_by_price_limit() {
        final var savedOrderIds = orderRepository.saveAll(getOrders()).stream()
                .map(Order::getId)
                .collect(toList());

        var orderDetails = builder()
                .sizeOfPage(3)
                .numberOfPage(0)
                .type("PAID")
                .priceFrom(valueOf(5.0))
                .priceTo(valueOf(35.0))
                .build();
        var filteredOrderIds = orderRepository.searchByFilters(orderDetails).getContent().stream()
                .map(Order::getId)
                .collect(toList());
        //validate FIRST:
        assertEquals(2, filteredOrderIds.size());
        assertTrue(savedOrderIds.containsAll(filteredOrderIds));

        orderDetails = builder()
                .sizeOfPage(3)
                .numberOfPage(0)
                .type("PAID")
                .priceFrom(valueOf(20.0))
                .priceTo(valueOf(30.0))
                .build();
        filteredOrderIds = orderRepository.searchByFilters(orderDetails).getContent().stream()
                .map(Order::getId)
                .collect(toList());
        //validate SECOND:
        assertEquals(1, filteredOrderIds.size());
        assertTrue(savedOrderIds.containsAll(filteredOrderIds));
    }

    private List<Order> getOrders() {
        final var classLoader = getClass().getClassLoader();
        final var objectMapper = new ObjectMapper();
        return of("orderOne.json", "orderTwo.json", "orderThree.json")
                .map(orderFileName -> classLoader.getResourceAsStream(RESOURCE_PREFIX_PATH.concat(orderFileName)))
                .filter(Objects::nonNull)
                .map(orderInputStream -> {
                    try {
                        return objectMapper.readValue(orderInputStream, Order.class);
                    } catch (Exception ex) {
                        return null;
                    }
                }).collect(toList());
    }
}