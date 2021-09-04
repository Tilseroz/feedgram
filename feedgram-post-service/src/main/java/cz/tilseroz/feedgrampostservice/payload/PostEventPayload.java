package cz.tilseroz.feedgrampostservice.payload;

import cz.tilseroz.feedgrampostservice.enums.PostEventTypeEnum;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class PostEventPayload {

    private Long id;
    private Long userId;
    private String username;
    private Instant createdAt;
    private Instant modifiedAt;
    private String lastModifiedBy;
    private String imageUrl;
    private String postMessage;
    private PostEventTypeEnum eventType;
}
