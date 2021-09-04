package cz.tilseroz.feedgrampostservice.payload;

import lombok.Data;

@Data
public class UpdateRequest {

    private Long postId;
    private String imageUrl;
    private String postMessage;
}
