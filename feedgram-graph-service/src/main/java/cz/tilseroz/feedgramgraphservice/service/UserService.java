package cz.tilseroz.feedgramgraphservice.service;

import cz.tilseroz.feedgramgraphservice.entity.Friendship;
import cz.tilseroz.feedgramgraphservice.entity.User;
import cz.tilseroz.feedgramgraphservice.exception.UsernameAlreadyExistsException;
import cz.tilseroz.feedgramgraphservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

    @Autowired
    UserRepository userRepository;

    public User createUser(User user) {

        if(userRepository.findByUsername(user.getUsername()).isPresent()) {
            String message = String.format("Username %s already exists", user.getUsername());
            log.warn(message);

            throw new UsernameAlreadyExistsException(message);
        }

        User saveUser = userRepository.save(user);

        log.info("User {} save successfully", saveUser.getUsername());

        return saveUser;
    }

    public void followUser(User follower, User following) {
        log.info("User {} will follow {}",
                follower.getUsername(), following.getUsername());

        User savedFollower = userRepository
                .findByUserId(follower.getUserId())
                .orElseGet(() -> {
                   log.info("User {} has not been found, so I will create it", follower.getUsername());

                   return this.createUser(follower);
                });

        User savedFollowing = userRepository
                .findByUserId(follower.getUserId())
                .orElseGet(() -> {
                    log.info("User {} has not been found, so I will create it", follower.getUsername());

                    return this.createUser(follower);
                });

//        if (savedFollower.getFriendships() == null) {
//            savedFollower.setFriendships(new HashSet<>());
//        }

        savedFollower
                .getFriendships()
                .add(Friendship.builder()
                        .startNode(savedFollower)
                        .endNode(savedFollowing)
                        .build());

        userRepository.save(savedFollower);
    }

    public boolean isFollowing(String usernameFollower, String usernameFollowedUser) {
        return userRepository.isFollowing(usernameFollower, usernameFollowedUser);
    }

}
