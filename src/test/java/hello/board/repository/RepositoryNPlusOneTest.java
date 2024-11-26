package hello.board.repository;

import hello.board.domain.Role;
import hello.board.domain.comment.Comment;
import hello.board.domain.message.Message;
import hello.board.domain.post.Post;
import hello.board.domain.user.User;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Transactional
public class RepositoryNPlusOneTest {

    @Autowired private EntityManager em;

    @Autowired private PostRepository postRepository;

    @Autowired private MessageRepository messageRepository;

    @Autowired private CommentRepository commentRepository;

    private Statistics statistics;

    @BeforeEach
    public void setUp() {
        Session session = em.unwrap(Session.class);
        statistics = session.getSessionFactory().getStatistics();
        statistics.setStatisticsEnabled(true);
    }

    private void getClear() {
        statistics.clear();
    }

    private void assertQueryCount(int queryCount) {
        assertThat(statistics.getQueryExecutionCount()).isEqualTo(queryCount);
    }

    private User persistUser() {
        User user = new User("test_user", "test_nickname", "password123", Role.USER);
        em.persist(user);
        getClear();
        return user;
    }


    @Test
    void test_findByUserOrderByPostDateDesc() throws Exception {
        //given
        User user = persistUser();

        //when
        postRepository.findByUserOrderByPostDateDesc(user);

        //then
        assertQueryCount(1);
    }

    @Test
    void test_receiver_message() throws Exception {
        //given
        User receiver = persistUser();

        //when
        messageRepository.findActiveReceivedMessages(receiver);

        //then
        assertQueryCount(1);
    }
    @Test
    void test_sender_message() throws Exception {
        //given
        User sender = persistUser();

        //when
        messageRepository.findActiveSentMessages(sender);

        //then
        assertQueryCount(1);
    }

    @Test
    void test_comment() throws Exception {
        //given
        User user = persistUser();


        //when
        commentRepository.findByUserOrderByCommentDateDesc(user);

        //then
        assertQueryCount(1);
    }
}
