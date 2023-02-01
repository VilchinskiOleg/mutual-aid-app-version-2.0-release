package order.orderservice.persistent.jpa.repository;

import order.orderservice.persistent.jpa.entity.TestModel;

import java.util.List;

public interface ExtendedTestModelRepository {

    List<TestModel> findByParams(String ... params);
}