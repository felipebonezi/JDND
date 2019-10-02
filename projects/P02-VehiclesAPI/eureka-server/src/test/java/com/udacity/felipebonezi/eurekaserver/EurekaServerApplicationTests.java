package com.udacity.felipebonezi.eurekaserver;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
public class EurekaServerApplicationTests {

	/**
	 * Load the local server port to be used as string format parameter on integration tests.
	 */
	@LocalServerPort
	private int serverPort;

	/**
	 * Helper class used to make integration tests with our Eureka Server.
	 */
	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void whenEurekaIsRunning_thenReturn200() {
		ResponseEntity<String> response = this.restTemplate.getForEntity(String.format("http://localhost:%d", this.serverPort), String.class);
		assert response != null && response.hasBody() && response.getStatusCode() == HttpStatus.OK;
	}

}
