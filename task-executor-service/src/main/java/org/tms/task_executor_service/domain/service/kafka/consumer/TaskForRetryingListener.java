package org.tms.task_executor_service.domain.service.kafka.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.CommitFailedException;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.mapper.autoconfiguration.mapper.Mapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.tms.common.kafka.avro.TaskEvent;
import org.tms.task_executor_service.domain.model.Task.Type;
import org.tms.task_executor_service.domain.service.CommandsGroupListenerAsyncManager;
import org.tms.task_executor_service.persistent.entity.Task;
import org.tms.task_executor_service.persistent.repository.TaskRepository;

import java.util.List;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

@Component
@Slf4j
@RequiredArgsConstructor
public class TaskForRetryingListener {

    public static final String GROUP_ID = "task-listener-group";
    private static final String RETRY_TASKS_TOPIC = "mutual-aid-retry-tasks-topic";
    private static final List<String> SUPPORTED_TYPE_OF_TASKS = stream(Type.values())
            .map(Enum::name)
            .collect(toList());

    private final TaskRepository taskRepository;
    private final CommandsGroupListenerAsyncManager commandsGroupManager;
    private final Mapper mapper;


    /**
     * Investigate: how to work with 'ConsumerRecords' properly ? [*]
     *
     * @param records - map which contains list of records per every partition in the topic.
     */
//    @KafkaListener(
//            topics = {RETRY_TASKS_TOPIC},
//            containerFactory = "taskEventListenerContainerFactory",
//            groupId = GROUP_ID)
    public void insertAllNewRetryableTasks(ConsumerRecords<String, TaskEvent> records) {
        ConsumerRecord<String, TaskEvent> currentRecord = null;
        try {
            var recordIterator = records.iterator();
            while (recordIterator.hasNext()) {
                currentRecord = recordIterator.next();
                processRecord(currentRecord);
            }
            //TODO: shift partition offset, or enable auto offset-reset! [*]
        } catch (Exception ex) {
            log.error("Unexpected error while processing record: topic={}, partition={}, key={}, offset={}, value={}",
                    RETRY_TASKS_TOPIC,
                    currentRecord.partition(),
                    currentRecord.key(),
                    currentRecord.offset(),
                    currentRecord.value(),
                    ex);
            //TODO: cash/save ids for processed task and exclude them in next processing iteration!
        }
    }

    @KafkaListener(
            topics = {RETRY_TASKS_TOPIC},
            containerFactory = "taskEventListenerContainerFactory",
            groupId = GROUP_ID)
    public void insertNewRetryableTask(ConsumerRecord<String, TaskEvent> record, Consumer<String, TaskEvent> consumer) {
        try {
            processRecord(record);
            shiftPartitionOffset(consumer, record);
        } catch (Exception ex) {
            log.error("Unexpected error while processing record: topic={}, partition={}, key={}, offset={}, value={}",
                    RETRY_TASKS_TOPIC,
                    record.partition(),
                    record.key(),
                    record.offset(),
                    record.value(),
                    ex);
        }
    }


    private void processRecord(ConsumerRecord<String, TaskEvent> record) {
        TaskEvent taskEvent = record.value();
        log.info("Process record: topic={}, key={}, offset={}, value={}",
                record.topic(), record.key(), record.offset(), taskEvent);

        var typeOfTask = (String) taskEvent.getType();
        if (SUPPORTED_TYPE_OF_TASKS.contains(typeOfTask) &&
                commandsGroupManager.isContainAppropriateListener(Type.valueOf(typeOfTask).getCommandsGroupListenerImplClass())) {
            var dataTask = mapper.map(taskEvent, Task.class);
            var savedDataTask = taskRepository.save(dataTask);
            log.info("Task={} was saved successfully", savedDataTask);
        }
        log.warn("TaskEvent={} from record with offset={} was skipped because it doesn't have appropriate listener or command implementation.",
                taskEvent, record.offset());
    }

    private void shiftPartitionOffset(Consumer<String, TaskEvent> consumer, ConsumerRecord<String, TaskEvent> record) {
        try {
            consumer.commitSync();
        } catch (CommitFailedException e) {
            log.error("Unexpected error while shift offset for listener: name={}, topic={}, partition={}, groupId={}",
                    this.getClass().getSimpleName(), RETRY_TASKS_TOPIC, record.partition(), GROUP_ID);
            // application-specific rollback of processed records
        }
    }
}