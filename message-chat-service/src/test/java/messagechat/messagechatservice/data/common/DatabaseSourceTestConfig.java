package messagechat.messagechatservice.data.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@DirtiesContext

@Slf4j
public abstract class DatabaseSourceTestConfig {

    private static final String TEST_DB_NAME = "messageChatServiceTEST";
    private static final String TEST_DB_USERNAME = "postgres";
    private static final String TEST_DB_PASSWORD = "postgres";

    @Container
    public static PostgreSQLContainer<?> postgresDB = new PostgreSQLContainer<>
            ("postgres")
            .withDatabaseName(TEST_DB_NAME)
            .withUsername(TEST_DB_USERNAME)
            .withPassword(TEST_DB_PASSWORD);


    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        // Test Container DB url:
        registry.add("datasource.message-chat.jdbc-url",postgresDB::getJdbcUrl);
        // Local DB url:
//        registry.add("datasource.message-chat.jdbc-url", () -> "jdbc:postgresql://localhost:5432/" + TEST_DB_NAME);
        registry.add("datasource.message-chat.username", () -> TEST_DB_USERNAME);
        registry.add("datasource.message-chat.password", () -> TEST_DB_PASSWORD);
    }


    protected void showTestDbUrl() {
        log.info("\n\nUse test DB by URL ={}\n", postgresDB.getJdbcUrl());
    }
}