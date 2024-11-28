package hello.board.service;

import hello.board.domain.user.UserForm;
import hello.board.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void duplicateUsername() throws Exception {
        //given
        UserForm userForm1 = new UserForm();
        userForm1.setUsername("testUser");
        userForm1.setNickname("tester1");
        userForm1.setPassword("password123");

        UserForm userForm2 = new UserForm();
        userForm2.setUsername("testUser");
        userForm2.setNickname("tester2");
        userForm2.setPassword("password123");

        //when
        userService.saveUser(userForm1);

        //then
        assertThatThrownBy(() -> userService.saveUser(userForm2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 존재하는 아이디입니다.");
    }
}