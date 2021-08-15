package event.event_storage_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "event.event_storage_service.persistent")
public class EventStorageServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventStorageServiceApplication.class, args);
    }

}
