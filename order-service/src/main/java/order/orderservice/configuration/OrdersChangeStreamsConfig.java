package order.orderservice.configuration;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.RequiredArgsConstructor;
import order.orderservice.domain.service.processor.EventManagerService;
import order.orderservice.persistent.mongo.entity.Order;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

import static com.mongodb.client.MongoClients.create;
import static java.util.Optional.ofNullable;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Configuration
@RequiredArgsConstructor
public class OrdersChangeStreamsConfig implements ApplicationListener<ApplicationReadyEvent> {

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;
    private final EventManagerService eventManagerService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder()
                .register("order.orderservice.persistent.mongo.entity")
                .automatic(true)
                .build());
        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);

        ConnectionString connectionString = new ConnectionString(mongoUri);

        MongoClientSettings clientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .codecRegistry(codecRegistry)
                .build();

        try (MongoClient mongoClient = create(clientSettings)) {
            MongoDatabase db = mongoClient.getDatabase(Objects.requireNonNull(connectionString.getDatabase()));
            MongoCollection<Order> orders = retrieveCollection(Order.class, db);
            orders.watch().forEach(eventManagerService::processEvent);
        } catch (Exception ex) {
            throw new RuntimeException("Ex.");
        }
    }


    private <T> MongoCollection<T> retrieveCollection(Class<T> pojoClazz, MongoDatabase db) {
        String collectionName = ofNullable(pojoClazz.getAnnotation(Document.class)).orElseThrow().collection();
        return db.getCollection(collectionName, pojoClazz);
    }
}