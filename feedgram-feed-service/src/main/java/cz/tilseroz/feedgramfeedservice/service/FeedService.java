package cz.tilseroz.feedgramfeedservice.service;


import com.datastax.oss.driver.api.core.cql.PagingState;
import cz.tilseroz.feedgramfeedservice.payload.FeedResult;
import cz.tilseroz.feedgramfeedservice.entity.UserFeed;
import cz.tilseroz.feedgramfeedservice.exception.ResourceNotFoundException;
import cz.tilseroz.feedgramfeedservice.model.Post;
import cz.tilseroz.feedgramfeedservice.util.AppConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.query.CassandraPageRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class FeedService {

//    @Autowired
//    private FeedRepository feedRepository;

    public FeedResult<Post> retrieveUserFeed(String username, Optional<String> pagingState) {
        log.info("Retrieving feed of user {}",
                username);

        CassandraPageRequest request = pagingState
                .map(pState -> CassandraPageRequest
                        .of(PageRequest.of(0, AppConstants.PAGE_SIZE), (ByteBuffer) PagingState.fromString(pState)))
                .orElse(CassandraPageRequest.first(AppConstants.PAGE_SIZE));

//        Slice<UserFeed> page = feedRepository.findByUsername(username, request);

//        if (page.isEmpty()) {
//            throw new ResourceNotFoundException(
//                    String.format("Feed not found for user %s", username));
//        }
//
//        String pageState = null;
//
//        if (!page.isLast()) {
//            pageState = ((CassandraPageRequest) page.getPageable())
//                    .getPagingState().toString();
//        }
//
//        List<Post> posts = getPost(page);
//
//        return FeedResult
//                .<Post>builder()
//                .content(posts)
//                .isLast(page.isLast())
//                .pagingState(pageState)
//                .build();
        return null;
    }

    private List<Post> getPost(Slice<UserFeed> page) {
        return null;
    }
}
