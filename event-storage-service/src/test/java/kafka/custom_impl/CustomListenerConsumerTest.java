package kafka.custom_impl;

import static org.awaitility.Awaitility.await;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Slf4j
public class CustomListenerConsumerTest {

  private final Properties config = new Properties();
  private List<String> results = new ArrayList<>();

  {
    config.put("group.id", "foo");
    config.put("bootstrap.servers", "localhost:9092");

    config.put("max.poll.interval.ms", 3000000);
    config.put("max.poll.records", 100);
  }

  @BeforeEach
  void initEach() {
    results.clear();
  }

  @Test
  void listen_events_and_shutdown_by_change_flag() {
    // Expects that you run docker container with Kafka and send events by CLI:
    config.put("enable.auto.commit", true);
    config.put("auto.offset.reset", "earliest"); // ?

    ExecutorService executorService = Executors.newFixedThreadPool(1);
    final var consumer = new ListenerConsumerStopByFlag(config, List.of("test"));
    executorService.submit(consumer);

    await()
        .atMost(10000, TimeUnit.SECONDS)
        .until(() -> results.size() >= 2);
    try {
      log.info("### run 'shutdown' from main thread ###");
      consumer.shutdown();
      log.info("### continue main thread ###");
    } catch (InterruptedException ex) {
      //...
      log.error("Error: thread was interrupted.", ex);
    }
    log.info("### Test is finished! ###");
  }

  private class ListenerConsumerStopByFlag implements Runnable {
    private final KafkaConsumer<String, String> consumer;
    private final List<String> topics;
    private final AtomicBoolean shutdown;
    private final CountDownLatch shutdownLatch;

    public ListenerConsumerStopByFlag(Properties config, List<String> topics) {
      this.consumer = new KafkaConsumer<>(config, new StringDeserializer(), new StringDeserializer());
      this.topics = topics;
      this.shutdown = new AtomicBoolean(false);
      this.shutdownLatch = new CountDownLatch(1);
    }


    public void run() {
      try {
        consumer.subscribe(topics);

        while (!shutdown.get()) {
          ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(500));
          process(records);
        }
      } finally {
        consumer.close();

        // for check that main thread is waiting:
        log.info("### waiting 3 sec before 'countDown' ###");
        try {
          Thread.sleep(3000);
        } catch (InterruptedException e) {
          log.error("Ex : ", e);
        }
        log.info("### do 'countDown' ###");

        shutdownLatch.countDown();
      }
    }

    public void process(ConsumerRecords<String, String> records) {
      log.info("Got record: {}", records);
      records.records("test").forEach(rec -> results.add(rec.value()));
    }

    public void shutdown() throws InterruptedException {
      shutdown.set(true);
      // main thread (test class) will sleep until 'final' block run countDown() :
      shutdownLatch.await();
    }
  }
}