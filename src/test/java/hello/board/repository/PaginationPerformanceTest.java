package hello.board.repository;

import hello.board.domain.Role;
import hello.board.domain.comment.Comment;
import hello.board.domain.post.Post;
import hello.board.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class PaginationPerformanceTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .username("testUser")
                .nickname("tester")
                .password("password")
                .grade(Role.USER)
                .build();

        userRepository.save(user);

        for (int i = 1; i <= 1000; i++) {
            Post post = Post.builder()
                    .title("Post " + i)
                    .content("Content for post " + i)
                    .user(user)
                    .build();
            postRepository.save(post);

            for (int j = 1; j <= 10; j++) {
                Comment comment = Comment.builder()
                        .content("Comment " + j + " for post " + i)
                        .user(user)
                        .post(post)
                        .build();
                commentRepository.save(comment);
            }
        }
    }
    
    @DisplayName("1000개의 게시글과 댓글을 생성해서 속도체크")
    @Test
    void paginationPerformance() throws Exception {
        //given
        int pageSize = 10;


        //when  //then
        for (int page = 0; page < 100; page++) {
            Pageable pageable = PageRequest.of(page, pageSize);

            long startTime = System.currentTimeMillis();
            Page<Post> posts = postRepository.findAll(pageable);
            long endTime = System.currentTimeMillis();

            System.out.println("Page " + (page + 1) + " loaded in " + (endTime - startTime) + "ms");
            assertThat(posts.getContent().size()).isEqualTo(pageSize);
        }
    }
}
