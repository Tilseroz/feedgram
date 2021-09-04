package cz.tilseroz.feedgrampostservice.messaging;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface PostEventChannel {

    String OUTPUT = "feedgramPostChanged";

    @Output(OUTPUT)
    MessageChannel feedgramPostChanged();
}
