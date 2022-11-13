package io.github.ntask_api.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.github.ntask_api.service.dto.MessageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.ZonedDateTime;

/**
 * A Message.
 */
@Entity
@Table(name = "message")
@Data
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@NoArgsConstructor
@EqualsAndHashCode(exclude = { "content", "isRead", "timestamp", "from", "room" })
@ToString(exclude = { "room" })
public class Message extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "content", columnDefinition = "text")
    private String content;

    @Column(name = "is_read")
    private Boolean isRead = Boolean.FALSE;

    @ManyToOne
    @JsonIgnoreProperties(value = { "users" }, allowSetters = true)
    private Event room;

    @ManyToOne
    private User from;

    public Message(MessageDTO dto) {
        content = dto.getContent();
        room = new Event(dto.getRoom());
        from = new User(dto.getFrom());
    }
}
