package io.github.ntask_api.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Set;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.github.ntask_api.service.dto.TaskDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ColumnDefault;

/**
 * A Task.
 */
@Entity
@Table(name = "t_task")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Data
@EqualsAndHashCode(exclude = { "startAt", "endAt", "event", "userTask" })
@ToString(exclude = { "startAt", "endAt", "event", "userTask" })
@NoArgsConstructor
public class Task extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_at")
    private ZonedDateTime startAt;

    @Column(name = "end_at")
    private ZonedDateTime endAt;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    private String name;

    @ColumnDefault("0")
    private Boolean isShow;

    @ManyToOne
    private Event event;

    @OneToMany(orphanRemoval = true, mappedBy = "task", cascade = CascadeType.ALL)
    private Set<UserTask> userTask;


    private Status status;

    public Task(TaskDTO taskDto) {
        startAt = taskDto.getStartAt();
        endAt = taskDto.getEndAt();
        description = taskDto.getDescription();
        name = taskDto.getName();
        status = Status.CREATED;
        event = new Event(taskDto.getEvent());
        isShow= taskDto.getIsShow();
    }

    public Task(Task task) {
        startAt = task.getStartAt();
        endAt = task.getEndAt();
        description = task.getDescription();
        name = task.getName();
        status = Status.CREATED;
        isShow= task.getIsShow();
    }

    public Task(Long id) {
        this.id = id;
    }

    public void setUserTask(Set<UserTask> userTask) {
        this.userTask.clear();
        if(userTask != null) {
            this.userTask.addAll(userTask);
        }
    }
}
