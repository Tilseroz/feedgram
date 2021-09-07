package cz.tilseroz.feedgramgraphservice.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserStatistics {

    long numberOfFollowing;
    long numberOfFollowers;
}
