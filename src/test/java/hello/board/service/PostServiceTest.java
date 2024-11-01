package hello.board.service;

import hello.board.domain.Role;
import hello.board.domain.post.Post;
import hello.board.domain.post.PostForm;
import hello.board.domain.user.User;
import hello.board.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void 글작성() throws Exception {
        //given
        User user = User.builder()
                .username("test")
                .nickname("닉네임")
                .password("1234")
                .grade(Role.USER)
                .build();
        ReflectionTestUtils.setField(user, "id", 1L);

        PostForm form = new PostForm();
        form.setTitle("테스트 제목");
        form.setContent("테스트 내용");

        Post savedPost = Post.builder()
                .title(form.getTitle())
                .content(form.getContent())
                .user(user)
                .build();
        ReflectionTestUtils.setField(savedPost, "id", 1L);

        when(postRepository.save(any(Post.class))).thenReturn(savedPost);

        //when
        Long postId = postService.savePost(form, String.valueOf(user));

        //then
        assertNotNull(postId);
        assertEquals(1L, postId);

        ArgumentCaptor<Post> postCaptor = ArgumentCaptor.forClass(Post.class);
        verify(postRepository, times(1)).save(postCaptor.capture());

        Post capturedPost = postCaptor.getValue();
        assertEquals("테스트 제목", capturedPost.getTitle());
        assertEquals("테스트 내용", capturedPost.getContent());
        assertEquals(user, capturedPost.getUser());
    }
}