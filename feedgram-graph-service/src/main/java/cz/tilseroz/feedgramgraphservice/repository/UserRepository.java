package cz.tilseroz.feedgramgraphservice.repository;

import cz.tilseroz.feedgramgraphservice.entity.User;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends Neo4jRepository<User, Long> {

    Optional<User> findByUserId(Long userId);
    Optional<User> findByUsername(String username);

    @Query("MATCH (n)-[r]->() where n.username=$username RETURN COUNT(r)")
    Long findCountFollowing(String username);

    @Query("MATCH (n)<-[r]-() where n.username=$username RETURN COUNT(r)")
    Long findCountFollowers(String username);

    @Query("MATCH (n1:User{ username:$usernameFollower }), (n2:User{username:$usernameFollowedUser }) RETURN EXISTS((n1)-[:IS_FOLLOWING]->(n2))")
    boolean isFollowing(String usernameFollower, String usernameFollowedUser);

    @Query("MATCH (n:User{username:$username})<--(f:User) Return f")
    List<User> retrieveFollowers(String username);

    @Query("MATCH (n:User{username:$username})-->(f:User) Return f")
    List<User> retrieveFollowing(String username);
}
