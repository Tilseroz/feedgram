package cz.tilseroz.feedgramauthservice.payload;

import cz.tilseroz.feedgramauthservice.enums.UserEventType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserEventPayload {

    private Long id;
    private String username;
    private String email;
    private String displayName;
    private UserEventType eventType;
}
