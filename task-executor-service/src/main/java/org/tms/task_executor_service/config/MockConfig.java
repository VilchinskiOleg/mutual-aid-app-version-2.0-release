package org.tms.task_executor_service.config;

import java.time.LocalDate;
import javax.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.tms.task_executor_service.persistent.entity.Task;
import org.tms.task_executor_service.persistent.entity.payload.ProfilePayload;
import org.tms.task_executor_service.persistent.repository.TaskRepository;

//@Configuration
//public class MockConfig {
//
//    @Resource
//    private TaskRepository taskRepository;
//
//    @EventListener(classes = ContextRefreshedEvent.class)
//    public void saveTaskMock() {
//        ProfilePayload profile = new ProfilePayload();
//        profile.setName("Name");
//        profile.setLastName("Lastname");
//        profile.setBirthday(LocalDate.now());
//
//        Task task = new Task();
//        task.setPayload(profile);
//
//        taskRepository.save(task);
//
//        Task savedTask = taskRepository.findAll().get(0);
//    }
//}