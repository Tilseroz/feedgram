package cz.tilseroz.feedgramapigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableEurekaClient
public class FeedgramApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeedgramApiGatewayApplication.class, args);
    }

}
