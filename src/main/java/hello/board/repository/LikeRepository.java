package hello.board.repository;

import hello.board.domain.like.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    @Query("SELECT l FROM Like l WHERE l.post.id = :postId AND l.user.username = :username")
    Optional<Like> findByPostIdAndUsername(@Param("postId") Long postId, @Param("username") String username);

    Long countByPostId(Long postId);
}
