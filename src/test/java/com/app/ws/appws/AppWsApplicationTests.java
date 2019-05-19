package com.app.ws.appws;

import com.app.ws.microservice.AppWsApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {AppWsApplication.class})
@TestPropertySource(
		locations = "classpath:application-test.properties")
public class AppWsApplicationTests {

	@Test
	public void contextLoads() {
	}

}
