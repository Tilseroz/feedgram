package cz.tilseroz.feedgramauthservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private Long id;

    @NotBlank
    @Size(max = 15)
    @Column(name = "user_name")
    private String username;

    @NotBlank
    @Size(max = 100)
    @Column(name = "user_password")
    private String password;

    @Email
    @Column(name = "mail")
    private String email;

    /**
     * Instant se používá pro uložení času do databáze. Na rozdíl od starého java.util.Date, které má přesnost na milisekundy, má Instant přesnost na nanosekundy.
     */
    @CreatedDate
    private Instant createdAt;

    private boolean active;
    private Set<Role> roles;

    public User(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.active = user.isActive();
        this.roles = user.getRoles();
    }
}
