package io.github.ntask_api.web.ws;

import io.github.ntask_api.domain.Message;
import io.github.ntask_api.repository.MessageRepository;
import io.github.ntask_api.service.dto.MessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessageSendingOperations messagingTemplate;
    private final MessageRepository repository;

    @MessageMapping("/chat.sendMessage")
    public MessageDTO sendMessage(@Payload MessageDTO chatMessage) {
        Objects.requireNonNull(chatMessage, "chatMessage");
        Objects.requireNonNull(chatMessage.getRoom(), "chatMessage.room");

        MessageDTO saved = new MessageDTO(repository.save(new Message(chatMessage)), Boolean.TRUE);
        saved.setClientTimestamp(chatMessage.getClientTimestamp());
        messagingTemplate.convertAndSend("/topic/" + chatMessage.getRoom(), saved);

        return saved;
    }

}
