package io.github.ntask_api.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * A Task.
 */
@Data
@NoArgsConstructor
public class NotificationDTO {

    private String description;
    private String title;
    private Set<Long> receivers;
    Long eventID;
    Long taskID;

}
