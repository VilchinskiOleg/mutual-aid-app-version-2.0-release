package com.example.testcontainertests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.testcontainertests.entity.MemberItemAutoIncrement;
import com.example.testcontainertests.repo.MemberItemAutoIncrementRepo;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * In this test we initial DB only by JPA auto DDL.
 * For that we add prop for auto generation : spring.jpa.hibernate.ddl-auto=create.
 */

@ExtendWith({ SpringExtension.class})
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

@Testcontainers
@DirtiesContext

@Disabled // only because they are global test end we can skip them during build project.
public class MemberItemContainerTestInitDbJpaDDL {

  @Container
  public static PostgreSQLContainer<?> postgresDB = new PostgreSQLContainer<>
      ("postgres:13.2")
      .withDatabaseName("testDB")
      .withUsername("postgres")
      .withPassword("postgres");

  @Autowired
  public MemberItemAutoIncrementRepo repo;

  @Test
  void saveAndReadEntity() {

    // create and check saved items:
    var firstMember = new MemberItemAutoIncrement();
    firstMember.setFirstName("first_name_test_1");
    firstMember.setLastName("last_name_test_1");

    var secondMember = new MemberItemAutoIncrement();
    secondMember.setFirstName("first_name_test_2");
    secondMember.setLastName("last_name_test_2");

    repo.save(firstMember);
    repo.save(secondMember);

    var firstSelect = repo.findById(1L);
    assertTrue(firstSelect.isPresent());
    var firstMemberAfterSaving = firstSelect.get();
    assertEquals(firstMember.getFirstName(), firstMemberAfterSaving.getFirstName());

    var secondSelect = repo.findById(2L);
    assertTrue(secondSelect.isPresent());
    var secondMemberAfterSaving = secondSelect.get();
    assertEquals(secondMember.getFirstName(), secondMemberAfterSaving.getFirstName());
  }


  @DynamicPropertySource
  public static void properties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url",postgresDB::getJdbcUrl);
    registry.add("spring.datasource.username", postgresDB::getUsername);
    registry.add("spring.datasource.password", postgresDB::getPassword);
    registry.add("spring.jpa.hibernate.ddl-auto", () -> "create");
    registry.add("spring.jpa.show-sql", () -> "true");
  }
}