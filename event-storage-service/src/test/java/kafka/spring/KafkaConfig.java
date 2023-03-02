package kafka.spring;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

public abstract class KafkaConfig {

  /**
   * Kafka config constant-values:
   */
  protected static final String BOOTSTRAP_SERVERS = "localhost:9092";
  protected static final Integer MAX_POLL_INTERVAL_MS = 3000000; //default value : 300000
  protected static final Integer MAX_POLL_RECORDS = 100; //default value : 500

  //default value : latest
  protected static final String LATEST = "latest";
  protected static final String EARLIEST = "earliest";



  protected Map<String, Object> defaultConsumerConfig = new HashMap<>();
  protected Map<String, Object> defaultProducerConfig = new HashMap<>();


  /**
   * Initialisation of configs:
   */
  {
    initConsumer();
    initProducer();
  }

  private void initConsumer() {
    defaultConsumerConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);

    defaultConsumerConfig.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, MAX_POLL_INTERVAL_MS);
    defaultConsumerConfig.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, MAX_POLL_RECORDS);

    defaultConsumerConfig.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
    defaultConsumerConfig.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 500);
    defaultConsumerConfig.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, LATEST);
  }

  private void initProducer() {
    defaultProducerConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
    defaultProducerConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    defaultProducerConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
  }
}