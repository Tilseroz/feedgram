package cz.tilseroz.feedgramgraphservice.service;

import cz.tilseroz.feedgramgraphservice.entity.UserStatistics;
import cz.tilseroz.feedgramgraphservice.entity.User;
import cz.tilseroz.feedgramgraphservice.exception.UsernameAlreadyExistsException;
import cz.tilseroz.feedgramgraphservice.exception.UsernameNotExistException;
import cz.tilseroz.feedgramgraphservice.payload.PagedResult;
import cz.tilseroz.feedgramgraphservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {

        if(userRepository.findByUsername(user.getUsername()).isPresent()) {
            String message = String.format("Username %s already exists", user.getUsername());
            log.warn(message);

            throw new UsernameAlreadyExistsException(message);
        }

        User savedUser = userRepository.save(user);

        log.info("User {} save successfully", savedUser.getUsername());

        return savedUser;
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
                .findByUserId(following.getUserId())
                .orElseGet(() -> {
                    log.info("User {} has not been found, so I will create it", following.getUsername());

                    return this.createUser(following);
                });

        if (savedFollower.getFriendships() == null) {
            savedFollower.setFriendships(new HashSet<>());
        }

        savedFollower.
                getFriendships().
                add(savedFollowing);
        userRepository.save(savedFollower);
    }

    public boolean isFollowing(String usernameFollower, String usernameFollowedUser) {
        return userRepository.isFollowing(usernameFollower, usernameFollowedUser);
    }

    public List<User> retrieveFollowers(String username) {
        List<User> followers = userRepository.retrieveFollowers(username);

        log.info("Found {} followers for user {}",
                followers.size(), username);

        return followers;
    }

    public List<User> retrieveFollowing(String username) {
        List<User> following = userRepository.retrieveFollowing(username);

        log.info("User {} is following {} users",
                username, following.size());

        return following;
    }

    public UserStatistics findFollowingFollowersStats(String username) {
        log.info("fetching degree for user {}", username);


        long following = userRepository.findCountFollowing(username);
        long followers = userRepository.findCountFollowers(username);

        log.info("User {} is following {} users and has {} followers", username, following, followers);

        return UserStatistics
                .builder()
                .numberOfFollowing(following)
                .numberOfFollowers(followers)
                .build();
    }

    public void updateUser(User user) {
        log.info("Updating user {}", user.getUsername());
        userRepository.findByUsername(user.getUsername())
                .map(savedUser -> {
                    savedUser.setName(user.getName());
                    savedUser.setUsername(user.getUsername());

                    savedUser = userRepository.save(savedUser);

                    log.info("User {} updated", savedUser);

                    return savedUser;
                })
                .orElseThrow(() -> {
                    log.info("User {} could not be updated",
                            user.getUsername());
                    throw new UsernameNotExistException(user.getUsername());
                });
    }

    public PagedResult<User> findPaginatedFollowers(String username, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<User> followers = userRepository.retrieveFollowers(username, pageable);
        log.info("found {} followers for user {}", followers.getTotalElements(), username);

        return buildPagedResult(followers);
    }

    private PagedResult<User> buildPagedResult(Page<User> page){
        return PagedResult
                .<User>builder()
                .content(page.getContent())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .page(page.getPageable().getPageNumber())
                .size(page.getSize())
                .last(page.isLast())
                .build();
    }
}
