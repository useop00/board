package hello.board.domain.message;

import hello.board.domain.user.User;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageForm {

    @NotNull(message = "수신자를 입력해주세요.")
    private String receiverUsername;

    private String receiverNickname;

    @NotEmpty(message = "메시지 내용을 입력해주세요.")
    private String content;

    public Message toEntity(User sender, User receiver) {
        return Message.builder()
                .sender(sender)
                .receiver(receiver)
                .content(this.content)
                .build();
    }
}
