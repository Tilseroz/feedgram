package cz.tilseroz.feedgramgraphservice.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class FollowUserPayload {

    @NotNull
    UserPayload follower;
    @NotNull
    UserPayload following;

}
