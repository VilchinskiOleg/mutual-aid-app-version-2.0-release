package kafka;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.tuple.Pair.of;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import event.event_storage_service.configuration.kafka.message.KafkaOrderEvent;
import event.event_storage_service.domain.service.processor.IdGeneratorService;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Slf4j
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {IdGeneratorService.class})
public class EssentialCasesKafkaTest extends BaseKafkaComponentTest {

  private final Map<Pair<String, String>, ConsumerRecord<String, KafkaOrderEvent>> RESULT_PUBLISHING_MAP = new HashMap();
  @Resource
  private IdGeneratorService idGenerator;

  {
    initKafkaTemplate();
  }

  @BeforeEach
  void initEach() {
    RESULT_PUBLISHING_MAP.clear();
  }


  /**
   * Test expect: every consumers will get a recorde, because they have different groups.
   *
   */
  @Test
  void send_messages_to_two_different_consumer_parallel_if_they_have_different_groupID() {

    final String group_A = "group_A";
    final String group_B = "group_B";
    final String topicName = "test_topic_0";

    // Set up consumer A:
    final String consumerA = this.setupAndStartContainer(group_A, topicName, null);

    // Set up consumer B:
    final String consumerB = this.setupAndStartContainer(group_B, topicName, null);

    // Publish messages and waite result:
    publishEventsAndWaiteResult(
        topicName,
        List.of(
            getMockedOrderEvent("CreateOrderKafkaEvent.json")
        ),
        2);

    //verify:
    final String msgPattern = "\n\nMessageListenerContainer with group={} and id={} : got record from topic={}, partition={}, key={}\n\n";

    var recordA = RESULT_PUBLISHING_MAP.get(of(group_A, consumerA));
    assertNotNull(recordA);
    log.info(msgPattern, group_A, consumerA, recordA.topic(), recordA.partition(), recordA.key());

    var recordB = RESULT_PUBLISHING_MAP.get(of(group_B, consumerB));
    assertNotNull(recordB);
    log.info(msgPattern, group_B, consumerB, recordB.topic(), recordB.partition(), recordB.key());

    assertEquals(recordA.value().getOrderId(), recordB.value().getOrderId());
  }


  /**
   * Test expect: only one consumer will get a recorde, because they have common group.
   *
   */
  @Test
  void send_messages_to_only_one_consumer_if_consumers_have_common_groupID() {

    final String group = "common_group";
    final String topicName = "test_topic_1";

    // Set up consumer A, it got uniq ID, but will work for the same topic and group like B:
    final String consumerA = this.setupAndStartContainer(group, topicName, null);

    // Set up consumer B, it got uniq ID, but will work for the same topic and group like A:
    final String consumerB = this.setupAndStartContainer(group, topicName, null);

    // Publish messages and waite result:
    publishEventsAndWaiteResult(
        topicName,
        List.of(
            getMockedOrderEvent("CreateOrderKafkaEvent.json")
        ),
        1);

    //verify:
    final String msgPattern = "\n\nMessageListenerContainer with group={} and id={} : got record from topic={}, partition={}, key={}\n\n";

    var recordA = RESULT_PUBLISHING_MAP.get(of(group, consumerA));
    var recordB = RESULT_PUBLISHING_MAP.get(of(group, consumerB));

    assertTrue(isNull(recordA) || isNull(recordB));

    var record = nonNull(recordA) ? recordA : recordB;
    var key = RESULT_PUBLISHING_MAP.entrySet().iterator().next().getKey();
    log.info(key.getLeft(), key.getRight(), consumerB, record.topic(), record.partition(), record.key());
  }



  private void initKafkaTemplate() {
    var factory = initializeProducerFactory(Map.of(
        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS,
        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class
    ));
    setupAndInitialKafkaTemplate(factory);
  }

  private String setupAndStartContainer(String group, String topicName, @Nullable Map<String, Object> extraProps) {
    Map<String, Object> consumerProps = Map.of(ConsumerConfig.GROUP_ID_CONFIG, group);
    if (MapUtils.isNotEmpty(extraProps)) {
      consumerProps.putAll(extraProps);
    }
    try {
      final String messageListenerContainerId = String.format("container-%s", idGenerator.generate());
      setupAndStartContainer(
          record -> {
            log.info("\n\nGot RECORD : TOPIC={}, PARTITION={}, OFFSET={}, KEY={}\n\n",
                record.topic(), record.partition(), record.offset(), record.key());
            RESULT_PUBLISHING_MAP.put(of(group, messageListenerContainerId), record);
          },
          topicName,
          initializeConsumerFactory(consumerProps),
          null,
          messageListenerContainerId
      );
      return messageListenerContainerId;
    } catch (Exception ex) {
      log.error("Unexpected exception during setup and run messageListenerContainer: ", ex);
      throw ex;
    }
  }

  private void publishEventsAndWaiteResult(String topicName, List<KafkaOrderEvent> publishedEvents, int resultMapSize) {
    publishedEvents.forEach(event -> kafkaTemplate.send(new ProducerRecord<>(topicName, event)));

    // waiting result:
    await()
        .atMost(10000, TimeUnit.SECONDS)
        .pollInterval(3, TimeUnit.SECONDS)
        .until(() -> {
          // sometimes have bug -> overwrating:
          return RESULT_PUBLISHING_MAP.size() >= resultMapSize;
        });

    // stop containers:
    stopContainers();
  }

  public KafkaOrderEvent getMockedOrderEvent(String fileName) {
    URL url = ClassLoader.getSystemClassLoader()
        .getResource("mockdata/".concat(fileName));
    if (nonNull(url)) {
      try {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new FileReader(url.getPath()), KafkaOrderEvent.class);
      } catch (IOException ex) {
        log.error("Cannot read data from JSON file {}", fileName, ex);
      }
    }
    throw new RuntimeException(String.format("Error reading mock data for file: {}", fileName));
  }
}