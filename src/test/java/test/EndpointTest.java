package test;

import static org.hamcrest.Matchers.containsString;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class EndpointTest extends Assert {

	private ConfigurableApplicationContext applicationContext;

	protected ConfigurableApplicationContext startApplication(boolean sensitive) throws Exception {
		List<String> args = new ArrayList<>();
		args.add("--endpoints.health.sensitive=" + sensitive);
		args.add("--endpoints.info.sensitive=" + sensitive);
		return Main.run(args.toArray(new String[args.size()]));
	}

	@After
	public void tearDown() {
		applicationContext.close();
	}

	private String executeGet(String path) throws Exception {
		String url = "http://localhost:8080" + path;
		String response = new RestTemplate().getForEntity(url, String.class).getBody();
		System.out.println("--------------------------------------");
		System.out.println("URL:      " + url);
		System.out.println("response: " + response);
		System.out.println("--------------------------------------");
		return response;

	}

	@Test
	public void testHealthEndpointIsProtected() throws Exception {
		this.applicationContext = startApplication(true);
		try {
			String response = executeGet("/health");
			fail("health endpoint is not protected, response: " + response);
		} catch (HttpClientErrorException ex) {
			assertEquals(401, ex.getStatusCode().value());
		}
	}

	@Test
	public void testHealthEndpointIsUnProtected() throws Exception {
		this.applicationContext = startApplication(false);
		String response = executeGet("/health");
		assertThat(response, containsString("UP"));
	}

	@Test
	public void testInfoEndpointIsProtected() throws Exception {
		this.applicationContext = startApplication(true);
		try {
			executeGet("/info");
			fail("is sensitive");
		} catch (HttpClientErrorException ex) {
			assertEquals(401, ex.getStatusCode().value());
		}
	}

	@Test
	public void testInfoEndpointIsUnProtected() throws Exception {
		this.applicationContext = startApplication(false);
		String response = executeGet("/info");
		assertEquals("{}", response);
	}
}