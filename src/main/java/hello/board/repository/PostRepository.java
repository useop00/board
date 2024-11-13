package hello.board.repository;


import hello.board.domain.post.Post;
import hello.board.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByUserOrderByPostDateDesc(User user);
}
