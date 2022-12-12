package io.github.ntask_api.web.ws;

import com.google.type.DateTime;
import io.github.ntask_api.domain.Message;
import io.github.ntask_api.domain.User;
import io.github.ntask_api.repository.MessageRepository;
import io.github.ntask_api.repository.UserRepository;
import io.github.ntask_api.service.NotificationService;
import io.github.ntask_api.service.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessageSendingOperations messagingTemplate;
    private final MessageRepository repository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @MessageMapping("/chat.sendMessage")
    public MessageDTO sendMessage(@Payload MessageDTO chatMessage) {
        Objects.requireNonNull(chatMessage, "chatMessage");
        Objects.requireNonNull(chatMessage.getRoom(), "chatMessage.room");

        MessageDTO saved = new MessageDTO(repository.save(new Message(chatMessage)), Boolean.TRUE);
        saved.setClientTimestamp(chatMessage.getClientTimestamp());
        messagingTemplate.convertAndSend("/topic/" + chatMessage.getRoom(), saved);

        return saved;
    }

    @PostMapping("/chat")
    public ResponseEntity createUserTask(@RequestBody ChatDTO chatDTO) {
        Set<User> users = new HashSet<>(userRepository.findAllById(chatDTO.getReceivers()));
        Notice notice = new Notice();
        notice.setContent(chatDTO.getDescription());
        notice.setSubject(chatDTO.getTitle());
        Map<String,String> data = new HashMap<>();
data.put("id", chatDTO.getEventId());
        data.put("type", "4");
        notice.setData(data);
        notice.setRegistrationTokens(users.stream().map(User::getNotificationKey).collect(Collectors.toList()));
        notificationService.sendNotification(notice);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/sendNotification")
    public ResponseEntity createNotification(@RequestBody NotificationDTO notificationDTO) {
        Set<User> users = new HashSet<>(userRepository.findAllById(notificationDTO.getReceivers()));
        Notice notice = new Notice();
        notice.setContent(notificationDTO.getDescription());
        notice.setSubject(notificationDTO.getTitle());
        Map<String,String> data = new HashMap<>();
        data.put("eventID", String.valueOf(notificationDTO.getEventID()));
        data.put("taskId", String.valueOf(notificationDTO.getTaskID()));
        data.put("type", "7");
        notice.setData(data);
        notice.setRegistrationTokens(users.stream().map(User::getNotificationKey).collect(Collectors.toList()));
        notificationService.sendNotification(notice);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/health-check")
    public String healthCheck() {
        return "Hello World";
    }

}
