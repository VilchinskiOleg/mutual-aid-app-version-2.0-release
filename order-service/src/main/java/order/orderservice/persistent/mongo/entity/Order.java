package order.orderservice.persistent.mongo.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@ToString
@Document(collection = "order-service_order")
public class Order {

    @BsonProperty("_id")
    @BsonId
    private ObjectId id;

    private String orderId;
    private String title;
    private String description;
    private Location location;
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal price;

    private String type;
    private String status;
    private String priority;

    private LocalDateTime createAt;
    private LocalDateTime modifyAt;

    private Member owner;
    private Set<Member> candidates;
    private Member executor;
}