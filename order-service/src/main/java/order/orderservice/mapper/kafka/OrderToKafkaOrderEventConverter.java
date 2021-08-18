package order.orderservice.mapper.kafka;

import order.orderservice.configuration.kafka.message.KafkaOrderEvent;
import order.orderservice.domain.model.Order;
import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;

@Component
public class OrderToKafkaOrderEventConverter extends BaseConverter<Order, KafkaOrderEvent> {

    @Override
    protected KafkaOrderEvent getDestination() {
        return new KafkaOrderEvent();
    }

    @Override
    public void convert(Order source, KafkaOrderEvent destination) {
        destination.setOrderId(source.getOrderId());
        destination.setStatus(mapper.map(source.getStatus()));
    }
}
