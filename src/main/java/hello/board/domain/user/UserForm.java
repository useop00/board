package hello.board.domain.user;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserForm {

    @NotEmpty(message = "닉네임은 필수입니다.")
    private String nickname;

    @NotEmpty(message = "아이디는 필수입니다.")
    private String username;

    @NotEmpty(message = "비밀번호는 필수입니다.")
    @Min(7)
    private String password;
}
