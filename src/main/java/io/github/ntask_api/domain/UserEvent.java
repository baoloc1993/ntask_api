package io.github.ntask_api.domain;

import java.io.Serializable;
import javax.persistence.*;

import io.github.ntask_api.service.dto.UserEventDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserGroup.
 */
@Entity
@Table(name = "user_group")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@EqualsAndHashCode
@ToString
public class UserEvent extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Authority role;

    @ManyToOne
    private User user;

    @ManyToOne
    private Event event;

    public UserEvent(Authority role, User user, Event event) {
        this.role = role;
        this.user = user;
        this.event = event;
    }

    public UserEvent(UserEventDTO userEventDto) {
        this(new Authority(userEventDto.getRole()), new User(userEventDto.getUser()), new Event(userEventDto.getEvent()));
    }

    public UserEvent() {
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Authority getRole() {
        return role;
    }

    public void setRole(Authority role) {
        this.role = role;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
