package cz.tilseroz.feedgramgraphservice.payload;

import lombok.Data;

@Data
public class UserPayload {

    private Long userId;
    private String username;
    private String name;
}
