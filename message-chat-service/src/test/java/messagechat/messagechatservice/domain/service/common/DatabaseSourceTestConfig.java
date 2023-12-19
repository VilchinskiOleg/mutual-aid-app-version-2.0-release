package messagechat.messagechatservice.domain.service.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.test.annotation.DirtiesContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@DirtiesContext

@Slf4j
public abstract class DatabaseSourceTestConfig {

    protected static final String TEST_DB_USERNAME = "postgres";
    protected static final String TEST_DB_PASSWORD = "postgres";
    private static final String TEST_DB_NAME = "messageChatServiceTEST";

    @Container
    public static PostgreSQLContainer<?> postgresDB = new PostgreSQLContainer<>
            ("postgres")
            .withDatabaseName(TEST_DB_NAME)
            .withUsername(TEST_DB_USERNAME)
            .withPassword(TEST_DB_PASSWORD);

    protected static void showTestDbUrl() {
        log.info("\n\nUse test DB by URL ={}\n", postgresDB.getJdbcUrl());
    }
}