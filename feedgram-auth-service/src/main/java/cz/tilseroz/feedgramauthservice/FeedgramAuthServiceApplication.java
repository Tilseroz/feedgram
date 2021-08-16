package cz.tilseroz.feedgramauthservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class FeedgramAuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FeedgramAuthServiceApplication.class, args);
	}

}
