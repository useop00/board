package hello.board.domain.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class MessageDto {

    private Long id;
    private String senderUsername;
    private String senderNickname;
    private String receiverUsername;
    private String receiverNickname;
    private String content;
    private LocalDateTime sentTime;
}
