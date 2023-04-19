package order.orderservice.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Cleanup;
import lombok.SneakyThrows;
import order.orderservice.persistent.mongo.entity.Order;
import order.orderservice.persistent.mongo.repository.OrderRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.annotation.Resource;
import java.io.InputStream;

@Testcontainers
@DirtiesContext

@Disabled //todo: delete after fixing!
@SpringBootTest
public class OrderChangesProcessingTest {

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
    @SneakyThrows
    void test() {
        final var classLoader = getClass().getClassLoader();
        final var objectMapper = new ObjectMapper();
        @Cleanup InputStream orderInStream = classLoader.getResourceAsStream(RESOURCE_PREFIX_PATH.concat("orderOne.json"));
        Order order = objectMapper.readValue(orderInStream, Order.class);

        orderRepository.save(order);

        System.out.println("OK");
    }
}