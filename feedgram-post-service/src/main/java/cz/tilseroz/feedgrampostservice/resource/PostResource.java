package cz.tilseroz.feedgrampostservice.resource;

import cz.tilseroz.feedgrampostservice.entity.Post;
import cz.tilseroz.feedgrampostservice.payload.ApiResponse;
import cz.tilseroz.feedgrampostservice.payload.PostRequest;
import cz.tilseroz.feedgrampostservice.payload.UpdateRequest;
import cz.tilseroz.feedgrampostservice.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.security.Principal;

@Slf4j
@RestController
public class PostResource {

    @Autowired
    PostService postService;

    @PostMapping("posts")
    public ResponseEntity<?> createPost(@Valid @RequestBody PostRequest postRequest) {
        log.info("Creating new post {} ", postRequest.getPostMessage());

        Post post = postService.createPost(postRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/posts/{id}")
                .buildAndExpand(post.getId()).toUri();

        return ResponseEntity
                .created(location)
                .body(new ApiResponse(true, "Post successfully created."));
    }

    @PutMapping("posts")
    public ResponseEntity<?> updatePost(@Valid @RequestBody UpdateRequest updateRequest) {
        log.info("Updating post {} ", updateRequest.getPostId());

        Post post = postService.updatePost(updateRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/posts/{id}")
                .buildAndExpand(post.getId()).toUri();

        return ResponseEntity
                .created(location)
                .body(new ApiResponse(true, "Post successfully updated"));
    }

    @DeleteMapping("posts/{id}")
    public void deletePost(@PathVariable("id") Long postId, @AuthenticationPrincipal Principal user) {
        log.info("Deleting post {} ", postId);
        postService.deletePost(postId, user.getName());
    }
}
