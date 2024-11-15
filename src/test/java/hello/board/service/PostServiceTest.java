package hello.board.service;

import hello.board.domain.Role;
import hello.board.domain.post.Post;
import hello.board.domain.post.PostForm;
import hello.board.domain.user.User;
import hello.board.repository.PostRepository;
import hello.board.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@Transactional
class PostServiceTest {

    @Mock private PostRepository postRepository;
    @Mock private EntityFinder entityFinder;
    @Mock private UserRepository userRepository;

    @InjectMocks
    private PostService postService;

    private User loginUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        loginUser = new User("loginUser", "nickname", "password", Role.USER);
        when(userRepository.save(any(User.class))).thenReturn(loginUser);
        when(entityFinder.getLoginUser(anyString())).thenReturn(loginUser);

    }

    @Test
    void savePost_성공() throws Exception {
        //given
        PostForm form = new PostForm();
        form.setTitle("test title");
        form.setContent("test content");

        Post post = Post.builder()
                .title(form.getTitle())
                .content(form.getContent())
                .user(loginUser)
                .build();
        ReflectionTestUtils.setField(post, "id", 1L);

        when(postRepository.save(any(Post.class))).thenReturn(post);

        //when
        Long postId = postService.savePost(form, null, loginUser.getUsername());

        //then
        assertNotNull(postId);
        verify(postRepository, times(1)).save(any(Post.class));
    }
}