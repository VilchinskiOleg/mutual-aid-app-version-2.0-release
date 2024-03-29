package order.orderservice.persistent.mongo.repository;

import order.orderservice.persistent.mongo.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends MongoRepository<Order, String>, ExtendedOrderRepository {

    Optional<Order> findByOrderId(String orderId);

    @Query(value = "{status: 'CLOSED', modifyAt: {$lte: ?0}}", sort = "{'createAt': 1}")
    Page<Order> findClosedOrders(LocalDateTime modifyAt, PageRequest pageRequest);

    @Query(value = "{'owner.memberId': ?0}")
    List<Order> searchByOwnerId(String memberId);
}
