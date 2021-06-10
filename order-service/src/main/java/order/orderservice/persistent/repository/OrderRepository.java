package order.orderservice.persistent.repository;

import order.orderservice.persistent.entity.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface OrderRepository extends MongoRepository<Order, String>, ExtendedOrderRepository {

    Optional<Order> findByOrderId(String orderId);
}
