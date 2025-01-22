package saeWeb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ServletComponentScan
@SpringBootApplication
@EntityScan(basePackages = "POJO")
@ComponentScan(basePackages = "saeWeb")
@EnableJpaRepositories(basePackages = "POJO")
public class SaeWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(SaeWebApplication.class, args);
	}

}
