package io.github.ntask_api.service.dto;

import io.github.ntask_api.domain.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A Event.
 */
@Data
@NoArgsConstructor
public class EventDTO {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private String description;

    private ZonedDateTime startAt;

    private ZonedDateTime endAt;

    private String status;

    private String exp;

    private Set<Long> task;

    private Set<UserDTO> members;

    private Set<TaskDTO> tasks;

    public EventDTO(Event e) {
        id = e.getId();
        name = e.getName();
        description = e.getDescription();
        startAt = e.getStartAt();
        endAt = e.getEndAt();
        status = e.getStatus().toString();
        exp = e.getExp();
        task = Optional.ofNullable(e.getTasks()).orElseGet(Collections::emptySet).stream().map(Task::getId).collect(Collectors.toSet());
        Map<User, Authority> ua = Optional.ofNullable(e.getUserEvents()).orElseGet(Collections::emptySet).stream().collect(Collectors.toMap(UserEvent::getUser, UserEvent::getRole, (a, b) -> b));
        members = ua.entrySet().stream().map(et -> new UserDTO(et.getKey(), et.getValue())).collect(Collectors.toSet());
        tasks = new HashSet<>();
    }

    public Set<Long> getTask() {
        if (task == null ) return new HashSet<>();
        return task;
    }
}
