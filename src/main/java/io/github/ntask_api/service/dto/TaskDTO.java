package io.github.ntask_api.service.dto;

import io.github.ntask_api.domain.Task;
import io.github.ntask_api.domain.UserTask;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A Task.
 */
@Data
@NoArgsConstructor
public class TaskDTO {

    private static final long serialVersionUID = 1L;

    private Long id;

    private ZonedDateTime startAt;

    private ZonedDateTime endAt;

    private String description;

    private String name;

    private Long event;

    private String status;

    private Boolean isShow;

    private Set<UserDTO> assignees;

    public TaskDTO(Task t) {
        id = t.getId();
        name = t.getName();
        startAt = t.getStartAt();
        endAt = t.getEndAt();
        description = t.getDescription();
        event = t.getEvent().getId();
        status = t.getStatus().toString();
        isShow = t.getIsShow();
        assignees = Optional.ofNullable(t.getUserTask()).orElseGet(Collections::emptySet).stream().map(UserTask::getUser).map(UserDTO::new).collect(Collectors.toSet());
    }

}
