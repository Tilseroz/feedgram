package cz.tilseroz.feedgramauthservice.messaging;

import cz.tilseroz.feedgramauthservice.entity.User;
import cz.tilseroz.feedgramauthservice.enums.UserEventType;
import cz.tilseroz.feedgramauthservice.payload.UserEventPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Service;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

/**
 * UserEventSender používá UserEventChannel k odesílání UserCreated a UserUpdated eventů do topicu feedgram.user.chanched.
 */
@Service
@Slf4j
public class UserEventSender {

    @Autowired
    private UserEventChannel userEventChannels;

    public void sendUserCreated(User user) {
        log.info("Sending message about creation of user. Username: {}", user.getUsername());

        sendUserChangedEvent(convertUserToUserEventPayload(user, UserEventType.CREATED));
    }

    public void sendUserUpdated(User user) {
        log.info("Sending message about update of user. Username: {}", user.getUsername());

        sendUserChangedEvent(convertUserToUserEventPayload(user, UserEventType.UPDATED));
    }

    /**
     * Odesílání payloadu do topicu
     * @param payload
     */
    private void sendUserChangedEvent(UserEventPayload payload) {
        Message<UserEventPayload> message =
                MessageBuilder
                        .withPayload(payload)
                        .setHeader(KafkaHeaders.MESSAGE_KEY, String.valueOf(payload.getId()))
                        .build();

        userEventChannels.feedgramUserChanged().send(message);

        log.info("User event {} has been sent to topic {} for user {}",
                message.getPayload().getEventType().name(),
                userEventChannels.OUTPUT,
                message.getPayload().getUsername());
    }

    private UserEventPayload convertUserToUserEventPayload(User user, UserEventType eventType) {
        return UserEventPayload
                .builder()
                .eventType(eventType)
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .build();
    }
}
