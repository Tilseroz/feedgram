package cz.tilseroz.feedgramfeedservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;

@SpringBootApplication
@EnableFeignClients
public class FeedgramFeedServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeedgramFeedServiceApplication.class, args);
    }

}
