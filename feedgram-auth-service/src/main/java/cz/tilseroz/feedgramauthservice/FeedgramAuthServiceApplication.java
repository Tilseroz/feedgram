package cz.tilseroz.feedgramauthservice;

import cz.tilseroz.feedgramauthservice.messaging.UserEventChannel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;

@SpringBootApplication
@EnableBinding(UserEventChannel.class)
public class FeedgramAuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FeedgramAuthServiceApplication.class, args);
	}

}
