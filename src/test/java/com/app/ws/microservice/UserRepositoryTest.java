package com.app.ws.microservice;

import com.app.ws.microservice.io.entity.UserEntity;
import com.app.ws.microservice.io.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource(locations="classpath:application-test.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    private static final Logger logger = LoggerFactory.getLogger(UserRepositoryTest.class);

    @Autowired
    private UserRepository userRepository;

    @Before
    public void init() {

        try (MySQLContainer mysql = (MySQLContainer) new MySQLContainer()
                .withDatabaseName("user_app")
                .withUsername("admin")
                .withPassword("admin")
                .withLogConsumer(new Slf4jLogConsumer(logger))) {
            mysql.start();
        }
    }

    @Test
    @Sql(scripts = "classpath:insert-userRegisteredEmail-True.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void shouldReturnUsersWithConfirmedEmail() {

        Pageable request = PageRequest.of(0, 1);
        Page<UserEntity> userEntities = userRepository.retrieveAllUsersWithConfirmedEmail(request);
        assertThat(userEntities).isNotNull();
        Stream<UserEntity> userEntityStream = userEntities.get();
        UserEntity userEntity = userEntityStream.findAny().orElse(null);
        assertThat(userEntity).isNotNull();
        assertThat(userEntity.getEmail()).isEqualTo("leandrotula@gmail.com");
        assertThat(userEntity.getFirstName()).isEqualTo("Leandro");
        assertThat(userEntity.getLastName()).isEqualTo("Tula");
        assertThat(userEntity.getEmailVerificationStatus()).isEqualTo(true);
    }

    @Test
    @Sql(scripts = "classpath:insert-userRegisteredEmail-True.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void shouldReturnUserByEmail() {

        UserEntity userEntity = userRepository.findByEmail("leandrotula@gmail.com");
        assertThat(userEntity).isNotNull();
        assertThat(userEntity.getFirstName()).isEqualTo("Leandro");
        assertThat(userEntity.getLastName()).isEqualTo("Tula");
        assertThat(userEntity.getEmailVerificationStatus()).isEqualTo(true);
    }

    @Test
    @Sql(scripts = "classpath:insert-userRegisteredEmail-True.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void shouldReturnUserByUserId() {

        UserEntity userEntity = userRepository.findByUserId("ABCZEQ1");
        assertThat(userEntity).isNotNull();
        assertThat(userEntity.getFirstName()).isEqualTo("Leandro");
        assertThat(userEntity.getLastName()).isEqualTo("Tula");
        assertThat(userEntity.getEmailVerificationStatus()).isEqualTo(true);
    }

    @After
    public void after() {
        userRepository.deleteAll();
    }
}
