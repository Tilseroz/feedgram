package cz.tilseroz.feedgramgraphservice.payload;

import lombok.Data;

@Data
public class FollowUserPayload {

    UserPayload follower;
    UserPayload following;

}
