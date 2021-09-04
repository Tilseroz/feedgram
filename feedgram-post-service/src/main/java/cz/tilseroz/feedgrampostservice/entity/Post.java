package cz.tilseroz.feedgrampostservice.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.Instant;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity(name = "Post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //TODO
//    @Column(name = "user_id")
//    private Long userId;

    @CreatedBy
    private String username;

    @CreatedDate
    @Column(name = "created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "modified_at")
    private Instant modifiedAt;

    @LastModifiedBy
    @Column(name = "modified_by")
    private String lastModifiedBy;

    @NonNull
    @Column(name = "image_url")
    private String imageUrl;

    @NonNull
    @Column(name = "post_message")
    private String postMessage;


}
