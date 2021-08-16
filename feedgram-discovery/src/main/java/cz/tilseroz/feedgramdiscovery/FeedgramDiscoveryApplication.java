package cz.tilseroz.feedgramdiscovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class FeedgramDiscoveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(FeedgramDiscoveryApplication.class, args);
	}

}
