package kafka;

import static java.util.Objects.nonNull;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.awaitility.Awaitility.await;

import com.fasterxml.jackson.databind.ObjectMapper;
import event.event_storage_service.EventStorageServiceApplication;
import event.event_storage_service.configuration.kafka.deserializer.KafkaOrderEventDeserializer;
import event.event_storage_service.configuration.kafka.message.KafkaOrderEvent;
import event.event_storage_service.domain.service.processor.IdGeneratorService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import javax.annotation.Resource;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.TopicPartitionOffset;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Slf4j
public abstract class BaseKafkaComponentTest {

  // Kafka props:
  protected static final String BOOTSTRAP_SERVERS = "localhost:9092";
//  protected static final String TOPIC = System.getenv("KAFKA_TOPIC");
  protected static final Integer MAX_POLL_INTERVAL_MS = 3000000; //default value : 300000
  protected static final Integer MAX_POLL_RECORDS = 100; //default value : 500

  //default value : latest
  protected static final String LATEST = "latest";
  protected static final String EARLIEST = "earliest";

  //default props:
  private Map<String, Object> consumerProps = new HashMap<>();
  protected KafkaTemplate<String, KafkaOrderEvent> kafkaTemplate;
  protected final ArrayList<KafkaMessageListenerContainer<String, KafkaOrderEvent>> messageListenerContainers = new ArrayList<>();

  {
    consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
    consumerProps.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, MAX_POLL_INTERVAL_MS);
    consumerProps.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, MAX_POLL_RECORDS);
    consumerProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
    consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, LATEST);
  }


  // Producer:
  protected ProducerFactory<String, KafkaOrderEvent> initializeProducerFactory(Map<String, Object> props) {
    return new DefaultKafkaProducerFactory<>(props);
  }

  protected void setupAndInitialKafkaTemplate(ProducerFactory<String, KafkaOrderEvent> producerFactory) {
    kafkaTemplate = new KafkaTemplate<>(producerFactory);
  }



  // Consumer:
  protected ConsumerFactory<String, KafkaOrderEvent> initializeConsumerFactory(Map<String, Object> props) {
    consumerProps.putAll(props);
    return new DefaultKafkaConsumerFactory<>(
        consumerProps,
        new StringDeserializer(),
        new KafkaOrderEventDeserializer(new ObjectMapper()));
  }

  protected void setupAndStartContainer(
      MessageListener<String, KafkaOrderEvent> messageListener,
      String topicName,
      ConsumerFactory<String, KafkaOrderEvent> consumerFactory,
      @Nullable
      List<TopicPartition> assignedPartitions,
      @Nullable
      String messageListenerContainerId) {

    KafkaMessageListenerContainer<String, KafkaOrderEvent> container;

    // initial container:
    if (isNotEmpty(assignedPartitions)) {
      var partitionOffsets = assignedPartitions
          .stream()
          .map(topicPartition -> new TopicPartitionOffset(topicName, topicPartition.partition()))
          .toArray(TopicPartitionOffset[]::new);
      container = new KafkaMessageListenerContainer<>(consumerFactory, new ContainerProperties(partitionOffsets));
    } else {
      container = new KafkaMessageListenerContainer<>(consumerFactory, new ContainerProperties(topicName));
    }
    container.setupMessageListener(messageListener);
    // start container:
    container.start();
    // waiting until kafka assign some partitions for managing by such listener-container:
    await()
        .atMost(10000, TimeUnit.SECONDS)
        .pollInterval(1, TimeUnit.SECONDS)
        .until(() -> isNotEmpty(container.getAssignedPartitions()));

    log.info("\n\nAssigned partitions for messageListenerContainer={} is: {}\n\n", messageListenerContainerId, container.getAssignedPartitions());
    messageListenerContainers.add(container);
  }

  protected void stopContainers() {
    if (!messageListenerContainers.isEmpty()) {
      for (KafkaMessageListenerContainer<String, KafkaOrderEvent> container : messageListenerContainers) {
        container.stop();
      }
    }
  }
}