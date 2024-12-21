package hello.board.domain.user;

import hello.board.domain.Role;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserForm {

    @NotEmpty(message = "닉네임은 필수입니다.")
    private String nickname;

    @NotEmpty(message = "아이디는 필수입니다.")
    @Size(min = 5, message = "아이디는 5자리 이상이어야 합니다.")
    private String username;

    @NotEmpty(message = "비밀번호는 필수입니다.")
    @Size(min = 8, message = "비밀번호는 8자리 이상이어야 합니다.")
    private String password;

    public User toEntity(String encodedPassword) {
        return User.builder()
                .username(this.username)
                .nickname(this.nickname)
                .password(encodedPassword)
                .grade(Role.USER)
                .build();
    }
}
