package cz.tilseroz.feedgrampostservice.service;

import cz.tilseroz.feedgrampostservice.entity.Post;
import cz.tilseroz.feedgrampostservice.exception.PostNotFoundException;
import cz.tilseroz.feedgrampostservice.messaging.PostEventSender;
import cz.tilseroz.feedgrampostservice.payload.PostRequest;
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

        Post post = updateCreatePostAndSendEvent(postRequest);

        log.info("Post {} is saved successfully for a user {}",
                post.getId(), post.getUsername());

        return post;
    }

    public Post updatePost(PostRequest postRequest) {
        log.info("Updating post - postMessage {}",postRequest.getPostMessage());

        Post post = updateCreatePostAndSendEvent(postRequest);

        log.info("Post {} is updated successfully for a user {}",
                post.getId(), post.getUsername());

        return post;
    }

    public void deletePost(Long postId, String username) {
        log.info("Deleting post {}", postId);

        postRepository
                .findById(postId)
                .map(post -> {
                    if (!post.getUsername().equals(username)) {
                        log.warn("User {} is not allowed to delete this post. postId: {}", username, postId);
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

    private Post updateCreatePostAndSendEvent(PostRequest postRequest) {
        Post post = new Post(postRequest.getImageUrl(), postRequest.getPostMessage());
        post = postRepository.save(post);
        postEventSender.sendPostCreated(post);
        return post;
    }

}
