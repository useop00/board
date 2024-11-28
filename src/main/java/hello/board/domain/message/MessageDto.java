package hello.board.domain.message;

import lombok.Builder;
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

    @Builder
    private MessageDto(Long id, String senderUsername, String senderNickname, String receiverUsername, String receiverNickname, String content, LocalDateTime sentTime) {
        this.id = id;
        this.senderUsername = senderUsername;
        this.senderNickname = senderNickname;
        this.receiverUsername = receiverUsername;
        this.receiverNickname = receiverNickname;
        this.content = content;
        this.sentTime = sentTime;
    }

    public static MessageDto of(Message message) {
        return MessageDto.builder()
                .id(message.getId())
                .senderUsername(message.getSender().getUsername())
                .senderNickname(message.getSender().getNickname())
                .receiverUsername(message.getReceiver().getUsername())
                .receiverNickname(message.getReceiver().getNickname())
                .content(message.getContent())
                .sentTime(message.getSentTime())
                .build();
    }
}
