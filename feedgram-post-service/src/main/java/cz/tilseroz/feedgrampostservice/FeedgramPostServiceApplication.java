package cz.tilseroz.feedgrampostservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableEurekaClient
@EnableBinding()
@EnableJpaAuditing
public class FeedgramPostServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeedgramPostServiceApplication.class, args);
    }

}
