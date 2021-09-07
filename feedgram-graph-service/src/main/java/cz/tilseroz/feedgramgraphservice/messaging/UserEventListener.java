package cz.tilseroz.feedgramgraphservice.messaging;

import cz.tilseroz.feedgramgraphservice.entity.User;
import cz.tilseroz.feedgramgraphservice.enums.UserEventType;
import cz.tilseroz.feedgramgraphservice.exception.PayloadException;
import cz.tilseroz.feedgramgraphservice.payload.UserEventPayload;
import cz.tilseroz.feedgramgraphservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserEventListener {

    @Autowired
    private UserService userService;

    @StreamListener(UserEventChannel.INPUT)
    public void onMessage(Message<UserEventPayload> userEventPayloadMessage) {
        if (userEventPayloadMessage == null) {
            throw new PayloadException("Payload message is null for topic feedgram.user.changed.");
        }

        UserEventType eventType = userEventPayloadMessage.getPayload().getEventType();

        log.info("Received message from feedgram.user.changed topic of type {}",
                eventType.name());

        Acknowledgment acknowledgment =
                userEventPayloadMessage.getHeaders().get(KafkaHeaders.ACKNOWLEDGMENT, Acknowledgment.class);

        User user = convertPayloadToUser(userEventPayloadMessage.getPayload());

        if (user == null) {
            throw new PayloadException("Payload is not valid. User is null.");
        }

        switch (eventType) {
            case CREATED:
                userService.createUser(user);
                break;
            case UPDATED:
                userService.updateUser(user);
                break;
        }

        if (acknowledgment != null) {
            acknowledgment.acknowledge();
        }
    }

    private User convertPayloadToUser(UserEventPayload payload) {
        return User
                .builder()
                .username(payload.getUsername())
                .name(payload.getDisplayName())
                .userId(payload.getId())
                .build();
    }
}
