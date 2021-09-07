package cz.tilseroz.feedgramgraphservice.entity;

import lombok.Builder;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;

@RelationshipEntity("IS_FOLLOWING")
@Builder
public class Friendship {

    @Id
    @GeneratedValue
    private Long id;

    @StartNode
    private User startNode;

    @EndNode
    private User endNode;
}
