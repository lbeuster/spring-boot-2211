package test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;

/**
 * @author lbeuster
 */
@EnableAutoConfiguration
@Configuration
public class Main {

	public static ConfigurableApplicationContext run(String[] args) throws Exception {
		SpringApplication app = new SpringApplication(Main.class);
		app.setShowBanner(true);
		app.setWebEnvironment(true);
		return app.run(args);
	}

	public static void main(String[] args) throws Exception {
		run(args);
	}
}