package cz.tilseroz.feedgramgraphservice.resource;

import cz.tilseroz.feedgramgraphservice.entity.User;
import cz.tilseroz.feedgramgraphservice.payload.ApiResponse;
import cz.tilseroz.feedgramgraphservice.payload.FollowUserPayload;
import cz.tilseroz.feedgramgraphservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Slf4j
public class UserApi {

    @Autowired
    private UserService userService;

    @PostMapping("/users/followers")
    public ResponseEntity<?> followUser(@Valid @RequestBody FollowUserPayload followUserPayload) {
        log.info("Request for following user {} by user {}",
                followUserPayload.getFollowing(), followUserPayload.getFollower());

        userService.followUser(
                User.builder()
                        .userId(followUserPayload.getFollower().getUserId())
                        .name(followUserPayload.getFollower().getName())
                        .username(followUserPayload.getFollower().getUsername())
                        .build(),

                User.builder()
                        .userId(followUserPayload.getFollowing().getUserId())
                        .name(followUserPayload.getFollowing().getName())
                        .username(followUserPayload.getFollowing().getUsername())
                        .build()

        );

        String message = String.format("User %s is following user %s",
                followUserPayload.getFollower().getUsername(),
                followUserPayload.getFollowing().getUsername());

        log.info(message);

        return ResponseEntity.ok().body(new ApiResponse(true, message));
    }

    @GetMapping("/users/{usernameFollower}/following/{usernameFollowedUser}")
    public ResponseEntity<?> isFollowing(@PathVariable String usernameFollower, @PathVariable String usernameFollowedUser) {
        log.info("Request for check if user {} is following user {}",
                usernameFollower, usernameFollowedUser);

        return ResponseEntity.ok(userService.isFollowing(usernameFollower, usernameFollowedUser));
    }

    @GetMapping("/users/{username}/followers")
    public ResponseEntity<?> retrieveFollowers(@PathVariable String username) {
        log.info("Request for retrieving followers of user {}", username);

        return ResponseEntity.ok(userService.retrieveFollowers(username));
    }

    @GetMapping("/users/{username}/following")
    public ResponseEntity<?> retrieveFollowing(@PathVariable String username) {
        log.info("Request for retrieving followers of user {}", username);

        return ResponseEntity.ok(userService.retrieveFollowing(username));
    }

    @GetMapping("/users/{username}/statistics")
    public ResponseEntity<?> findFollowingFollowersStats(@PathVariable String username) {
        log.info("Received request to get node degree for {}", username);

        return ResponseEntity.ok(userService.findFollowingFollowersStats(username));
    }
}
