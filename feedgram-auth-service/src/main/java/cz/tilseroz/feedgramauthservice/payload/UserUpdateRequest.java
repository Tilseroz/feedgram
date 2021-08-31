package cz.tilseroz.feedgramauthservice.payload;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Data
public class UserUpdateRequest {

    @Size(max = 100)
    private String name;

    @Size(max = 15)
    private String username;

    @Email
    private String email;
}
