package messagechat.messagechatservice.configuration.data;

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

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Map;

/**
 * In order to add additional persistent unit (configuration) with other entity set and DB connection properties ->
 * you need just create new other beans 'EntityManagerFactory.class' and 'TransactionManager.class' with necessary other details (processing entities + props).
 *
 * In order to test only that service without docker-compose file -> just run that command in docker CLI:
 * docker run --name postgres-container -e POSTGRES_DB=${DB_NAME} -e POSTGRES_USER=${PG_USERNAME} -e POSTGRES_PASSWORD=${PG_PASSWORD} -p 5432:5432 -it postgres
 */
@Configuration
@EnableJpaRepositories(basePackages = "messagechat.messagechatservice.persistent.repository")
@EnableTransactionManagement(proxyTargetClass = true)
public class MessageChatJpaConfig {

    private static final String API_DATA_HANDLER_PERSISTENT_UNIT = "message-chat-persistent-unit";

    @Bean(name = "dataSource")
    @ConfigurationProperties(prefix = "datasource.message-chat")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                       DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("messagechat.messagechatservice.persistent.entity")
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
                "hibernate.show_sql", "true",
                "hibernate.format_sql", "true",
                "hibernate.physical_naming_strategy", "org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy"
        );
    }
}