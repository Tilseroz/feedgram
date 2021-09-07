package cz.tilseroz.feedgramgraphservice;

import cz.tilseroz.feedgramgraphservice.messaging.UserEventChannel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@SpringBootApplication
@EnableBinding(UserEventChannel.class)
@EnableNeo4jRepositories
public class FeedgramGraphServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeedgramGraphServiceApplication.class, args);
    }

}
