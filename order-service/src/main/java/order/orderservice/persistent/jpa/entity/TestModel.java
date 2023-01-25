package order.orderservice.persistent.jpa.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "test_model")

@Getter
@Setter
@NoArgsConstructor
public class TestModel {

    @Id
    //TODO: Try to create your own sequence based on UUID and use it for generate ID.
    // Play with GenerationType.SEQUENCE strategy:
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String value;
//    private LocalDateTime createdAt;

    public TestModel(String value) {
        this.value = value;
    }
}