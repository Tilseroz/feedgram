package cz.tilseroz.feedgrampostservice.payload;

import lombok.Data;

@Data
public class PostRequest {

    private String imageUrl;
    private String postMessage;
}
