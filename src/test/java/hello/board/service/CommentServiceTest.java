package hello.board.service;

import hello.board.domain.comment.Comment;
import hello.board.domain.comment.CommentForm;
import hello.board.domain.post.Post;
import hello.board.domain.post.PostForm;
import hello.board.domain.user.UserForm;
import hello.board.service.comment.CommentService;
import hello.board.service.post.PostService;
import hello.board.service.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Autowired
    private EntityFinder entityFinder;

    @DisplayName("권한 없는 사용자는 댓글을 달지 못한다.")
    @Test
    void checkAuthorization() throws Exception {
        //given
        UserForm userForm = new UserForm();
        userForm.setUsername("test");
        userForm.setNickname("tester");
        userForm.setPassword("password123");
        Long userId = userService.saveUser(userForm);

        PostForm postForm = new PostForm();
        postForm.setTitle("Test Post");
        postForm.setContent("Test Content");
        Long postId = postService.savePost(postForm, null, userForm.getUsername());

        CommentForm commentForm = new CommentForm();
        commentForm.setContent("Test Comment");
        commentForm.setPostId(postId);

        //when
        Long commentId = commentService.saveComment(commentForm, userForm.getUsername());

        Comment savedComment = entityFinder.getComment(commentId);
        Post associatePost = savedComment.getPost();

        //then
        assertThat(savedComment).isNotNull();
        assertThat(associatePost).isNotNull();
        assertThat(associatePost.getId()).isEqualTo(postId);
    }

}