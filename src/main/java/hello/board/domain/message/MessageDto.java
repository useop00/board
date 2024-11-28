package hello.board.domain.message;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MessageDto {

    private Long id;
    private String senderUsername;
    private String senderNickname;
    private String receiverUsername;
    private String receiverNickname;
    private String content;
    private LocalDateTime sentTime;

    public MessageDto(Long id, String senderUsername, String senderNickname, String receiverUsername, String receiverNickname, String content, LocalDateTime sentTime) {
        this.id = id;
        this.senderUsername = senderUsername;
        this.senderNickname = senderNickname;
        this.receiverUsername = receiverUsername;
        this.receiverNickname = receiverNickname;
        this.content = content;
        this.sentTime = sentTime;
    }
}
