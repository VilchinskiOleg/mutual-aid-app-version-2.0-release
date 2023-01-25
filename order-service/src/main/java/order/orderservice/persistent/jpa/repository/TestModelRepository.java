package order.orderservice.persistent.jpa.repository;

import order.orderservice.persistent.jpa.entity.TestModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestModelRepository extends JpaRepository<TestModel, Long> {

}