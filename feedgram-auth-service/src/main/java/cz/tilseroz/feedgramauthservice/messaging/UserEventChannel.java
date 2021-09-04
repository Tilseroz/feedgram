package cz.tilseroz.feedgramauthservice.messaging;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface UserEventChannel {

    String OUTPUT = "feedgramUserChanged";

    /**
     * "Output" znamená, že jde o kanál, která bude sloužit k publikování zpráv
     */
    @Output(OUTPUT)
    MessageChannel feedgramUserChanged();
}
