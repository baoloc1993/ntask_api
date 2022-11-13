package io.github.ntask_api.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;

import io.github.ntask_api.service.dto.UserTaskDTO;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserTask.
 */
@Entity
@Table(name = "user_task")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Data
@EqualsAndHashCode(exclude = { "task", "user" })
@ToString(exclude = { "task", "user" })
@NoArgsConstructor
@AllArgsConstructor
public class UserTask extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties(value = { "event" }, allowSetters = true)
    private Task task;

    @ManyToOne
    private User user;

    public UserTask(UserTaskDTO userTaskDto) {
        this(null, new Task(userTaskDto.getTask()), new User(userTaskDto.getUser()));
    }
}
