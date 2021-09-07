package cz.tilseroz.feedgramgraphservice.entity;

import lombok.Builder;
import lombok.Data;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NodeEntity
public class User {

    @Id
    @GeneratedValue
    private Long id;
    private Long userId;
    private String username;
    private String name;

    @Relationship(type = "IS_FOLLOWING")
    private Set<Friendship> friendships;
}
