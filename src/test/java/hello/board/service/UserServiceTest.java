package hello.board.service;

import hello.board.domain.Role;
import hello.board.domain.user.User;
import hello.board.domain.user.UserForm;
import hello.board.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void 회원가입() throws Exception {
        //given
        UserForm form = new UserForm();
        form.setUsername("test");
        form.setNickname("닉네임");
        form.setPassword("password1234");

        String encodePassword = "encodePassword1234";
        User savedUser = User.builder()
                .username("test")
                .nickname("닉네임")
                .password(encodePassword)
                .grade(Role.USER)
                .build();

        ReflectionTestUtils.setField(savedUser, "id", 1L);

        when(bCryptPasswordEncoder.encode("password1234")).thenReturn(encodePassword);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        //when
        Long userId = userService.saveUser(form);

        //then
        assertNotNull(userId);
        assertEquals(1L, userId);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userCaptor.capture());

        User capturedUser = userCaptor.getValue();
        assertEquals("test", capturedUser.getUsername());
        assertEquals("닉네임", capturedUser.getNickname());
        assertEquals(encodePassword, capturedUser.getPassword());
        assertEquals(Role.USER, capturedUser.getGrade());

    }
    @Test
    void 중복검사() throws Exception {
        //given
        UserForm form = new UserForm();
        form.setUsername("test1");
        form.setNickname("닉네임");
        form.setPassword("1234");

        User existingUser = User.builder()
                .username("test1")
                .nickname("닉네임")
                .password("existingPassword")
                .grade(Role.USER)
                .build();

        when(userRepository.findByUsername("test1")).thenReturn(Optional.of(existingUser));

        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                    userService.saveUser(form);
        });

        //then
        assertEquals("이미 존재하는 사용자입니다", exception.getMessage());

        verify(bCryptPasswordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }
}