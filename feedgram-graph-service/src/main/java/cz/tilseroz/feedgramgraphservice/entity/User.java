package cz.tilseroz.feedgramgraphservice.entity;

import lombok.Builder;
import lombok.Data;


import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.Set;

@Data
@Builder
@Node
public class User {

    @Id
    @GeneratedValue
    private Long id;
    private Long userId;
    private String username;
    private String name;

    @Relationship(type = "IS_FOLLOWING")
    private Set<User> friendships;
}
