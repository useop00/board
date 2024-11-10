package hello.board.domain.message;

import hello.board.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    private String content;

    @Column(name = "sent_time")
    private LocalDateTime sentTime;

    @Column(name = "is_read")
    private boolean isRead = false;

    // 발신자와 수신자의 개별 삭제 여부
    private boolean isDeletedBySender = false;
    private boolean isDeletedByReceiver = false;

    @Builder
    public Message(User sender, User receiver, String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.sentTime = LocalDateTime.now();
    }

    // 발신자에 의한 삭제
    public void markDeletedBySender() {
        this.isDeletedBySender = true;
    }

    // 수신자에 의한 삭제
    public void markDeletedByReceiver() {
        this.isDeletedByReceiver = true;
    }
}
