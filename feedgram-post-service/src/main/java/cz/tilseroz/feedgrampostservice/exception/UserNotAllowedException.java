package cz.tilseroz.feedgrampostservice.exception;

public class UserNotAllowedException extends RuntimeException {

    public UserNotAllowedException(String username, Long postId) {
        super(String.format("User %s is not allowed to delete this post with id %s", username, postId));
    }
}
