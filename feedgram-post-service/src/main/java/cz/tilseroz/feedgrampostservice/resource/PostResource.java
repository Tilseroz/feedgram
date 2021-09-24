package cz.tilseroz.feedgrampostservice.resource;

import cz.tilseroz.feedgrampostservice.entity.Post;
import cz.tilseroz.feedgrampostservice.payload.ApiResponse;
import cz.tilseroz.feedgrampostservice.payload.PostRequest;
import cz.tilseroz.feedgrampostservice.payload.UpdateRequest;
import cz.tilseroz.feedgrampostservice.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import java.util.List;

@Slf4j
@RestController
public class PostResource {

    @Autowired
    private PostService postService;

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
    public ResponseEntity<?> deletePost(@PathVariable("id") Long postId, @AuthenticationPrincipal String user) {
        log.info("Deleting post {} ", postId);
        postService.deletePost(postId, user);

        return ResponseEntity.ok(new ApiResponse(true, String.format("Post %d successfully deleted", postId)));
    }

    @PostMapping("posts/in")
    public ResponseEntity<?> findPostsByIdIn(@RequestBody List<String> ids) {
        log.info("Finding posts for {} ids.",
                ids.size());

        List<Post> posts = postService.findByIdInOrderByCreatedAtDesc(ids);

        log.info("Found {} posts.", ids.size());

        return ResponseEntity.ok(posts);
    }
}
