package com.app.ws.appws.io.repository;

import com.app.ws.microservice.io.entity.UserEntity;
import com.app.ws.microservice.io.repository.UserRepository;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.builder.ImageFromDockerfile;

import java.util.UUID;
import java.util.function.Consumer;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {UserRepository.class})
@TestPropertySource(locations="classpath:application-test.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @ClassRule
    public static GenericContainer mysql = new GenericContainer(new ImageFromDockerfile("mysql-userapp")
            .withDockerfileFromBuilder(dockerfileBuilder -> {
                dockerfileBuilder.from("mysql:5.6")
                        .env("MYSQL_ROOT_PASSWORD", "admin")
                        .env("MYSQL_DATABASE", "user_app")
                        .env("MYSQL_USER", "admin")
                        .env("MYSQL_ALLOW_EMPTY_PASSWORD", "no")
                        .env("MYSQL_PASSWORD", "admin");
            }))
            .withExposedPorts(3306)
            .withCreateContainerCmdModifier(
                    new Consumer<CreateContainerCmd>() {
                        @Override
                        public void accept(CreateContainerCmd createContainerCmd) {
                            createContainerCmd.withPortBindings(new PortBinding(Ports.Binding.bindPort(3306), new ExposedPort(3306)));
                        }
                    }
            )
            .waitingFor(Wait.forListeningPort());



    @Autowired
    private UserRepository userRepository;

    @Before
    public void init() {

        UserEntity user = new UserEntity();
        user.setFirstName("Leandro");
        user.setLastName("Tula");
        user.setUserId(UUID.randomUUID().toString());
        user.setEmailVerificationToken("ABCDEFG");
        user.setEmailVerificationStatus(true);
        userRepository.save(user);

    }

    @Test
    public void shouldReturnUsersWithConfirmedEmail() {

        Pageable request = PageRequest.of(0, 2);
        Page<UserEntity> userEntities = userRepository.retrieveAllUsersWithConfirmedEmail(request);
        Assert.assertNotNull(userEntities);
    }
}
