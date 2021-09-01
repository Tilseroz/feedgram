package cz.tilseroz.feedgramauthservice.entity;

import cz.tilseroz.feedgramauthservice.enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

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
    @Column(name = "created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "modified_at")
    private Instant modifiedAt;

    @Column(name = "user_role")
    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    @Column
    private boolean active;

    public User(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.active = user.isActive();
        this.role = user.getRole();
        this.createdAt = user.getCreatedAt();
        this.modifiedAt = user.getModifiedAt();
    }
}
