package hello.board.domain;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserForm {

    @NotEmpty(message = "아이디는 필수입니다.")
    private String username;

    private String password;
}
