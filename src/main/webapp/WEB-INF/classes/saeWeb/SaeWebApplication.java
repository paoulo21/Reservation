package saeWeb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "POJO")
@EnableJpaRepositories(basePackages = "POJO")
public class SaeWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(SaeWebApplication.class, args);
	}

}
