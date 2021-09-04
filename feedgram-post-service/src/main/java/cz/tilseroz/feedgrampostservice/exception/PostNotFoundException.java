package cz.tilseroz.feedgrampostservice.exception;

public class PostNotFoundException extends RuntimeException {

    public PostNotFoundException(Long postId) {
        super(String.format("Post %s was not found", postId));
    }
}
