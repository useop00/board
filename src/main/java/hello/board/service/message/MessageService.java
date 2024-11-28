package hello.board.service.message;

import hello.board.domain.message.Message;
import hello.board.domain.message.MessageDto;
import hello.board.domain.message.MessageForm;
import hello.board.domain.user.User;
import hello.board.repository.MessageRepository;
import hello.board.service.EntityFinder;
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

        Message message = form.toEntity(sender, receiver);

        messageRepository.save(message);
        return message.getId();
    }

    public List<MessageDto> getReceivedMessages(String username) {
        User receiver = entityFinder.getLoginUser(username);

        return mapMessagesToDto(messageRepository.findActiveReceivedMessages(receiver));

    }

    public List<MessageDto> getSentMessages(String username) {
        User sender = entityFinder.getLoginUser(username);

        return mapMessagesToDto(messageRepository.findActiveSentMessages(sender));
    }


    public MessageDto getMessageDtoById(Long messageId) {
        Message message = entityFinder.getMessage(messageId);

        return MessageDto.of(message);
    }


    @Transactional
    public void deleteAllMessages(String username) {
        User user = entityFinder.getLoginUser(username);

        // 수신자에 의해 삭제
        messageRepository.findActiveReceivedMessages(user)
                .forEach(Message::markDeletedByReceiver);

        // 발신자에 의해 삭제
        messageRepository.findActiveSentMessages(user)
                .forEach(Message::markDeletedBySender);
    }

    private List<MessageDto> mapMessagesToDto(List<Message> messages) {
        return messages.stream()
                .map(MessageDto::of)
                .toList();
    }


}
