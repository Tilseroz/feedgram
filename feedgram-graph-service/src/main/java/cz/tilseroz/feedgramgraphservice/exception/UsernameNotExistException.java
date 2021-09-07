package cz.tilseroz.feedgramgraphservice.exception;

public class UsernameNotExistException extends RuntimeException {

    public UsernameNotExistException(String username) {
        super(String.format("Username % has not been found.", username));
    }
}
