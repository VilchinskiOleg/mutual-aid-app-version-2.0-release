package event.event_storage_service.persistent.repository;

import event.event_storage_service.persistent.entity.OrderEvent;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderEventRepository extends MongoRepository<OrderEvent, String> {

    List<OrderEvent> findByOrderDetails_OrderId(String orderId);
}
