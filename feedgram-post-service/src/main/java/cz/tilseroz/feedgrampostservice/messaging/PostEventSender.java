package cz.tilseroz.feedgrampostservice.messaging;

import cz.tilseroz.feedgrampostservice.entity.Post;
import cz.tilseroz.feedgrampostservice.enums.PostEventTypeEnum;
import cz.tilseroz.feedgrampostservice.payload.PostEventPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
@Slf4j
public class PostEventSender {

    @Inject
    private PostEventChannel postEventChannel;

    public void sendPostCreated(Post post) {
        log.info("Sending post created event for post id {}", post.getId());
        sendPostEventChanged(convertPostToPayload(post, PostEventTypeEnum.CREATED));
    }

    public void sendPostUpdated(Post post) {
        log.info("Sending post updated event for post {}", post.getId());
        sendPostEventChanged(convertPostToPayload(post, PostEventTypeEnum.UPDATED));
    }

    public void sendPostDeleted(Post post) {
        log.info("Sending post deleted event for post {}", post.getId());
        sendPostEventChanged(convertPostToPayload(post, PostEventTypeEnum.DELETED));
    }

    private void sendPostEventChanged(PostEventPayload postEventPayload) {

        Message<PostEventPayload> message =
                MessageBuilder
                        .withPayload(postEventPayload)
                        .setHeader(KafkaHeaders.MESSAGE_KEY, postEventPayload.getId())
                        .build();

        postEventChannel.feedgramPostChanged().send(message);

        log.info("Post event {} sent to topic {} for post {} and user {}",
                message.getPayload().getEventType().name(),
                postEventChannel.OUTPUT,
                message.getPayload().getId(),
                message.getPayload().getUsername());
    }

    private PostEventPayload convertPostToPayload(Post post, PostEventTypeEnum eventType) {
        return PostEventPayload
                .builder()
                .eventType(eventType)
                .id(post.getId())
                .username(post.getUsername())
                .createdAt(post.getCreatedAt())
                .modifiedAt(post.getModifiedAt())
                .lastModifiedBy(post.getLastModifiedBy())
                .imageUrl(post.getImageUrl())
                .postMessage(post.getPostMessage())
                .build();
    }
}
