package cz.tilseroz.feedgramfeedservice.resource;

import cz.tilseroz.feedgramfeedservice.Payload.FeedResult;
import cz.tilseroz.feedgramfeedservice.model.Post;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@Slf4j
public class FeedApi {

    @RequestMapping("/feed/{username}")
    public ResponseEntity<FeedResult<Post>> getFeed(
            @PathVariable String username,
            @RequestParam(value = "ps", required = false) Optional<String> pagingState) {

        log.info("Fetching feed for user {}",
                username);

        //TODO feedservice
        return ResponseEntity.ok("");
    }
}
