package org.tms.profile_service_core.domain.service.kafka.producer;

import io.confluent.kafka.schemaregistry.ParsedSchema;
import io.confluent.kafka.schemaregistry.avro.AvroSchema;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.common.http.autoconfiguration.model.CommonData;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.tms.common.kafka.avro.TaskEvent;
import org.tms.profile_service_core.domain.model.Profile;

import javax.annotation.PostConstruct;
import java.net.ConnectException;
import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProfileTaskForRetryingProducer {

    private static final String TOPIC = "mutual-aid-retry-tasks-topic";
    private static final String KEY = "profile-retry-task-key";
    private static final int MAX_RETRY = 7;

    private final SchemaRegistryClient schemaRegistryClient;
    private final KafkaTemplate<String, TaskEvent> kafkaTemplate;
    private final CommonData commonData;

    private int attemptsCounter = MAX_RETRY;


    /**
     * Connection Exception {@link ConnectException} can occur here trying to get connection to Kafka during some time,
     * because it can take some time before listener will be reade for connection.
     *
     * So that it's necessary to implement retry mechanism.
     *
     * It's necessary to wrap exception into unchecked exception, since {@link ConnectException} - is checked exception.
     */
    @PostConstruct
    public void init() {
        try {

            // Reset schema:
            schemaRegistryClient.reset();
            for (String subject : schemaRegistryClient.getAllSubjects()) {
                this.schemaRegistryClient.deleteSubject(subject);
            }

            // Register schema:
            ParsedSchema avroSchema = new AvroSchema(TaskEvent.getClassSchema().toString());
            var res = schemaRegistryClient.register(TOPIC + "-value", avroSchema);

            log.info("Registered avroSchema for topic= {} successfully", TOPIC);
        } catch (Exception e) {
            log.warn("Wasn't able to register avroSchema for topic= {}. Attempt= {}. Reason: {}",
                    TOPIC, MAX_RETRY - --attemptsCounter, e.getMessage());
            if (attemptsCounter > 0 ) {

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }

                init();
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    public void sendTaskEvent(Exception ex, Profile profile, String type) {
        var task = TaskEvent.newBuilder()
                .setType(type)
                .setErrorCode("500") // cannot set null value or ignore
                .setErrorMessage(ex.getLocalizedMessage())
                .setFlowId(commonData.getFlowId())
                .setPayload(profile.toString()).build();
        final var record = new ProducerRecord<>(TOPIC, null, null, KEY, task, null);
        final SendResult<String, TaskEvent> result;
        try {
            result = kafkaTemplate.send(record).get();
        } catch (ExecutionException | InterruptedException e) {
            log.error("Er.");
            throw new RuntimeException(e);
        }
        final RecordMetadata metadata = result.getRecordMetadata();
        log.info("Inf.");
    }
}
