package hello.board.controller;

import hello.board.domain.message.MessageDto;
import hello.board.domain.message.MessageForm;
import hello.board.service.EntityFinder;
import hello.board.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;
    private final EntityFinder entityFinder;

    @GetMapping("/send")
    public String sendMessageForm(@RequestParam(value = "receiverUsername", required = false) String receiverUsername,
                                  Model model) {
        MessageForm messageForm = new MessageForm();

        if (receiverUsername != null) {
            String receiverNickname = entityFinder.getUserNickname(receiverUsername);
            messageForm.setReceiverUsername(receiverUsername);
            messageForm.setReceiverNickname(receiverNickname);
        }

        model.addAttribute("messageForm", messageForm);
        return "messages/sendMessage";
    }
    @PostMapping("/send")
    public String sendMessage(@Validated @ModelAttribute("messageForm") MessageForm form,
                              BindingResult result, Principal pri) {
        if (result.hasErrors()) {
            return "messages/sendMessage";
        }
        String senderUsername = pri.getName();
        messageService.sendMessage(senderUsername, form);
        return "redirect:/";
    }

    @GetMapping("/{id}")
    @ResponseBody
    public MessageDto getMessage(@PathVariable Long id) {
        return messageService.getMessageDtoById(id);
    }

    @PostMapping("/deleteAll")
    public String deleteAllMessages(Principal principal) {
        messageService.deleteAllMessages(principal.getName());
        return "redirect:/mypage";
    }
}
