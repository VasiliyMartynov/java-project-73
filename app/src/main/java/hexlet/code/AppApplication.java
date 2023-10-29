package hexlet.code;

import hexlet.code.controllers.TaskController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class AppApplication {

	static final Logger logger =
			LoggerFactory.getLogger(AppApplication.class);

	public static void main(String[] args) {
		logger.info("!!!!!!!!!!!!!!!!!!!!!!!!!");
		SpringApplication.run(AppApplication.class, args);

	}
}
