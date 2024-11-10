package hello.board.service;

import hello.board.domain.message.Message;
import hello.board.domain.message.MessageDto;
import hello.board.domain.message.MessageForm;
import hello.board.domain.user.User;
import hello.board.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final EntityFinder entityFinder;

    @Transactional
    public Long sendMessage(String senderUsername, MessageForm form) {
        User sender = entityFinder.getLoginUser(senderUsername);
        User receiver = entityFinder.getLoginUser(form.getReceiverUsername());

        Message message = Message.builder()
                .sender(sender)
                .receiver(receiver)
                .content(form.getContent())
                .build();

        messageRepository.save(message);
        return message.getId();
    }

    public List<MessageDto> getReceivedMessages(String username) {
        User receiver = entityFinder.getLoginUser(username);

        return messageRepository.findActiveReceivedMessages(receiver).stream()
                .map(message -> new MessageDto(
                        message.getId(),
                        message.getSender().getUsername(),
                        message.getSender().getNickname(),
                        message.getReceiver().getUsername(),
                        message.getReceiver().getNickname(),
                        message.getContent(),
                        message.getSentTime()))
                .toList();
    }

    public List<MessageDto> getSentMessages(String username) {
        User sender = entityFinder.getLoginUser(username);

        return messageRepository.findActiveSentMessages(sender).stream()
                .map(message -> new MessageDto(
                        message.getId(),
                        message.getSender().getUsername(),
                        message.getSender().getNickname(),
                        message.getReceiver().getUsername(),
                        message.getReceiver().getNickname(),
                        message.getContent(),
                        message.getSentTime()))
                .toList();
    }

    public MessageDto getMessageDtoById(Long messageId) {
        Message message = entityFinder.getMessage(messageId);

        return new MessageDto(
                message.getId(),
                message.getSender().getUsername(),
                message.getSender().getNickname(),
                message.getReceiver().getUsername(),
                message.getReceiver().getNickname(),
                message.getContent(),
                message.getSentTime()
        );
    }

    @Transactional
    public void deleteAllMessages(String username) {
        User user = entityFinder.getLoginUser(username);

        // 수신자에 의해 삭제 처리
        List<Message> receivedMessages = messageRepository.findActiveReceivedMessages(user);
        for (Message message : receivedMessages) {
            message.markDeletedByReceiver();
        }

        // 발신자에 의해 삭제 처리
        List<Message> sentMessages = messageRepository.findActiveSentMessages(user);
        for (Message message : sentMessages) {
            message.markDeletedBySender();
        }
    }
}
