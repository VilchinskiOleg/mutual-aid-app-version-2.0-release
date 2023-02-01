package order.orderservice.persistent.jpa.repository;

import order.orderservice.persistent.jpa.entity.TestModel;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class ExtendedTestModelRepositoryImpl implements ExtendedTestModelRepository {

    @Resource
    private EntityManager entityManager;

    @Override
    public List<TestModel> findByParams(String... params) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<TestModel> query = criteriaBuilder.createQuery(TestModel.class);
        Root<TestModel> postRoot = query.from(TestModel.class);

        //TODO: .. Add logic for work with Criteria ..

        return null;
    }
}