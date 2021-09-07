package cz.tilseroz.feedgramgraphservice.messaging;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface UserEventChannel {

    String INPUT = "feedgramUserChanged";

    @Input(INPUT)
    SubscribableChannel feedgramUserChanged();
}
