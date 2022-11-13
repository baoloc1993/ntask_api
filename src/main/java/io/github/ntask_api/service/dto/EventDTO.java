package io.github.ntask_api.service.dto;

import io.github.ntask_api.domain.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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

    private Set<Long> tasks;

    private Set<UserDTO> members;

    public EventDTO(Event e) {
        id = e.getId();
        name = e.getName();
        description = e.getDescription();
        startAt = e.getStartAt();
        endAt = e.getEndAt();
        status = e.getStatus().toString();
        exp = e.getExp();
        tasks = Optional.ofNullable(e.getTasks()).orElseGet(Collections::emptySet).stream().map(Task::getId).collect(Collectors.toSet());
        Map<User, Authority> ua = Optional.ofNullable(e.getUserEvents()).orElseGet(Collections::emptySet).stream().collect(Collectors.toMap(UserEvent::getUser, UserEvent::getRole, (a, b) -> b));
        members = ua.entrySet().stream().map(et -> new UserDTO(et.getKey(), et.getValue())).collect(Collectors.toSet());
    }

}
