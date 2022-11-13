package io.github.ntask_api.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.*;

import io.github.ntask_api.service.dto.EventDTO;
import io.github.ntask_api.service.dto.UserDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Event.
 */
@Entity
@Table(name = "event")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Data
@EqualsAndHashCode(exclude = { "userEvents", "startAt", "endAt", "status", "tasks" })
@ToString(exclude = { "userEvents", "startAt", "endAt", "status", "tasks" })
@NoArgsConstructor
public class Event extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "start_at")
    private ZonedDateTime startAt;

    @Column(name = "end_at")
    private ZonedDateTime endAt;

    private Status status;

    @Column(name = "exp", columnDefinition = "text")
    private String exp;

    @OneToMany(orphanRemoval = true, mappedBy = "event", cascade = CascadeType.ALL)
    private Set<Task> tasks;

    @OneToMany(orphanRemoval = true, mappedBy = "event")
    private Set<UserEvent> userEvents;

    public Event(Long id) {
        this.id = id;
    }

    public Event(EventDTO eventDto) {
        name = eventDto.getName();
        description = eventDto.getDescription();
        startAt = eventDto.getStartAt();
        endAt = eventDto.getEndAt();
        status = Status.CREATED;
        exp = eventDto.getExp();
    }

}
