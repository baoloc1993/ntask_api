package io.github.ntask_api.service.dto;

import io.github.ntask_api.domain.UserTask;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A UserTask.
 */
@Data
@NoArgsConstructor
public class UserTaskDTO {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long task;

    private Long user;

    public UserTaskDTO(UserTask ut) {
        id = ut.getId();
        task = ut.getTask().getId();
        user = ut.getUser().getId();
    }
}
