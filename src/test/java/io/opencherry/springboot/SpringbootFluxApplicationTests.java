package io.opencherry.springboot;

import io.opencherry.springboot.flux.model.URI;
import io.opencherry.springboot.flux.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringbootFluxApplicationTests {

	protected static Logger logger = LoggerFactory.getLogger(ReactorTests.class);

	@Autowired
	private WebTestClient webTestClient;


	@Before
	public void before() {
		logger.info("before------------------");
	}

	@After
	public void after() {
		logger.info("after------------------");
	}

	@Test
	public void testCollect() {
		User user = new User();
		user.setId(100l);
		user.setUsername("simon");
		webTestClient.post().uri(URI.URI_COLLECT)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.body(Mono.just(user), User.class)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
				.expectBody()
				.jsonPath("$.password").isNotEmpty()
				.jsonPath("$.password").isEqualTo("{\"code\":0,\"message\":\"success\",\"data\":[]}");
	}


}
