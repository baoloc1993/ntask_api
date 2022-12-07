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
public class ChatDTO {

    private String description;
    private String title;
    private String eventId;
    private Set<Long> receivers;

}
