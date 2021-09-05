package cz.tilseroz.feedgrampostservice.service;

import cz.tilseroz.feedgrampostservice.entity.Post;
import cz.tilseroz.feedgrampostservice.exception.PostNotFoundException;
import cz.tilseroz.feedgrampostservice.exception.UserNotAllowedException;
import cz.tilseroz.feedgrampostservice.messaging.PostEventSender;
import cz.tilseroz.feedgrampostservice.payload.PostRequest;
import cz.tilseroz.feedgrampostservice.payload.UpdateRequest;
import cz.tilseroz.feedgrampostservice.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostEventSender postEventSender;

    public Post createPost(PostRequest postRequest) {
        log.info("Creating post - postMessage {}", postRequest.getPostMessage());

        Post post = new Post(postRequest.getImageUrl(), postRequest.getPostMessage());
        post = postRepository.save(post);
        postEventSender.sendPostCreated(post);

        log.info("Post {} is saved successfully for a user {}",
                post.getId(), post.getUsername());

        return post;
    }

    public Post updatePost(UpdateRequest updateRequest) {
        log.info("Updating post - postMessage {}", updateRequest.getPostId());

        Post updatedPost = postRepository
                .findById(updateRequest.getPostId())
                .map(post -> {
                    boolean changed = false;
                    if (!updateRequest.getPostMessage().isBlank()) {
                        post.setPostMessage(updateRequest.getPostMessage());
                        changed = true;
                    }
                    if (!updateRequest.getImageUrl().isBlank()) {
                        post.setImageUrl(updateRequest.getImageUrl());
                        changed = true;
                    }

                    if (changed) {
                        post = postRepository.save(post);
                        postEventSender.sendPostUpdated(post);
                    } else {
                        log.info("Received request for update of post {}, but nothing was updated. No changes.",
                                updateRequest.getPostId());
                    }

                    return post;
                })
                .orElseThrow(() -> {
                    log.warn("Post {} does not exist, so I couln't update it", updateRequest.getPostId());
                    return new PostNotFoundException(updateRequest.getPostId());
                });

        return updatedPost;
    }

    public void deletePost(Long postId, String username) {
        log.info("Deleting post {}", postId);

        postRepository
                .findById(postId)
                .map(post -> {
                    if (!username.equals(post.getUsername())) {
                        log.warn("User {} is not allowed to delete this post. postId: {}", username, postId);
                        throw new UserNotAllowedException(username, postId);
                    }

                    postRepository.delete(post);
                    postEventSender.sendPostDeleted(post);
                    return post;
                })
                .orElseThrow(() -> {
                   log.warn("Post {} was not found.", postId);
                   return new PostNotFoundException(postId);
                });
    }

}
