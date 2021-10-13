package cz.tilseroz.feedgramfeedservice.payload;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FeedResult<T> {

    private String pagingState;
    private boolean isLast;
    private List<T> content;
}
