package cz.tilseroz.feedgramfeedservice.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.Instant;

@Data
@Builder
@ToString
public class Post {

    private Long id;
    private String username;
    private String lastModifiedBy;
    private String postText;
    private Instant createdAt;
    private Instant updatedAt;
}
