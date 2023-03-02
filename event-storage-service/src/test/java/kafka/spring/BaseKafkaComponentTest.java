package kafka.spring;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.awaitility.Awaitility.await;

import com.fasterxml.jackson.databind.ObjectMapper;
import event.event_storage_service.configuration.kafka.deserializer.KafkaOrderEventDeserializer;
import event.event_storage_service.configuration.kafka.message.KafkaOrderEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.TopicPartitionOffset;

@Slf4j
public abstract class BaseKafkaComponentTest extends KafkaConfig {

  protected KafkaTemplate<String, KafkaOrderEvent> kafkaTemplate;
  protected final ArrayList<KafkaMessageListenerContainer<String, KafkaOrderEvent>> messageListenerContainers = new ArrayList<>();


  protected void setupAndInitialKafkaTemplate(@Nullable Map<String, Object> props) {
    var producerFactory = initializeProducerFactory(props);
    kafkaTemplate = new KafkaTemplate<>(producerFactory);
  }

  /**
   * Initialise ProducerFactory.
   *
   * @param props - extra props for producer config which you want to add
   * @return ProducerFactory<String, KafkaOrderEvent>
   */
  protected ProducerFactory<String, KafkaOrderEvent> initializeProducerFactory(@Nullable Map<String, Object> props) {
    final var producerConfig = new HashMap<>(super.defaultProducerConfig);
    if (MapUtils.isNotEmpty(props)) {
      producerConfig.putAll(props);
    }
    return new DefaultKafkaProducerFactory<>(producerConfig);
  }

  /**
   * Initialise ConsumerFactory.
   *
   * @param props - extra props for consumer config which you want to add
   * @return ConsumerFactory<String, KafkaOrderEvent>
   */
  protected ConsumerFactory<String, KafkaOrderEvent> initializeConsumerFactory(@Nullable Map<String, Object> props) {
    final var consumerConfig = new HashMap<>(super.defaultConsumerConfig);
    if (MapUtils.isNotEmpty(props)) {
      consumerConfig.putAll(props);
    }
    return new DefaultKafkaConsumerFactory<>(
        consumerConfig,
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