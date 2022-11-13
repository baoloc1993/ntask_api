package io.github.ntask_api.service.dto;

import io.github.ntask_api.domain.UserEvent;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserEventDTO {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long role;

    private Long user;

    private Long event;

    public UserEventDTO(UserEvent ug) {
        id = ug.getId();
        role = ug.getRole().getId();
        user = ug.getUser().getId();
        event = ug.getEvent().getId();
    }

}
