package io.github.ntask_api.service.dto;

import io.github.ntask_api.domain.Message;
import lombok.Data;

import java.io.Serializable;
import java.time.ZonedDateTime;

@Data
public class MessageDTO implements Serializable {

    private Long id;

    private String content;

    private Boolean isRead;

    private Boolean isSaved;

    private ZonedDateTime createdDate;

    private ZonedDateTime clientTimestamp;

    private Long room;

    private Long from;

    public MessageDTO(){}

    public MessageDTO(Message message, Boolean isSaved) {
        id = message.getId();
        content = message.getContent();
        isRead = message.getIsRead();
        this.isSaved = isSaved;
        createdDate = message.getCreatedDate();
        room = message.getRoom().getId();
        from = message.getFrom().getId();
    }
}
