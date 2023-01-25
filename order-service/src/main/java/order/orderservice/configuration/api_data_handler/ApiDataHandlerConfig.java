package order.orderservice.configuration.api_data_handler;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import javax.sql.DataSource;
import javax.persistence.EntityManagerFactory;
import java.util.Map;
import java.util.Properties;

/**
 * In order to add additional persistent unit (configuration) with other entity set and DB connection properties ->
 * you need just create other new the similar config (like this) with necessary other details.
 *
 * In order to test only that service without docker-compose file -> just run that command in docker CLI:
 * docker run --name postgres-container -e POSTGRES_DB=apiDB -e POSTGRES_USER=admintest -e POSTGRES_PASSWORD=postgres12345 -p 5432:5432 -it postgres
 */
@Configuration
@EnableJpaRepositories(basePackages = "order.orderservice.persistent.jpa.repository")
@EnableTransactionManagement(proxyTargetClass = true)
public class ApiDataHandlerConfig {

    private static final String API_DATA_HANDLER_PERSISTENT_UNIT = "api.data.handler.persistent.unit";

    @Bean(name = "dataSource")
    @ConfigurationProperties(prefix = "datasource.api")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                       DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("order.orderservice.persistent.jpa.entity")
                .persistenceUnit(API_DATA_HANDLER_PERSISTENT_UNIT)
                .properties(getProperties())
                .build();
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    private Map<String, Object> getProperties() {
        return Map.of(
                "hibernate.dialect", "org.hibernate.dialect.PostgreSQL10Dialect",
                "hibernate.hbm2ddl.auto", "update",
                "hibernate.show_sql", "true"
        );
    }
}