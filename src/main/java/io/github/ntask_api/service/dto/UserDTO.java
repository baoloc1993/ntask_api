package io.github.ntask_api.service.dto;

import io.github.ntask_api.domain.Authority;
import io.github.ntask_api.domain.Event;
import io.github.ntask_api.domain.User;
import io.github.ntask_api.domain.UserEvent;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class UserDTO implements Serializable {

    protected static final long serialVersionUID = 1L;

    protected Long id;
    protected String login;
    protected String name;
    protected String email;
    protected String bio;
    protected String avatarUrl;
    protected byte[] avatar;
    protected Set<String> roles;
    protected Set<Long> bookmarkEvents;

    protected Authority eventRole;

    public UserDTO(User user) {
        this.id = user.getId();
        // Customize it here if you need, or not, firstName/lastName/etc
        this.login = user.getLogin();
        name = user.getName();
        email = user.getEmail();
        bio = user.getBio();
        avatarUrl = user.getAvatarUrl();
        roles = Optional.ofNullable(user.getAuthorities()).orElseGet(Collections::emptySet).stream().map(Authority::getName).collect(Collectors.toSet());
        bookmarkEvents = Optional.ofNullable(user.getBookmarkEvents()).orElseGet(Collections::emptySet).stream().map(Event::getId).collect(Collectors.toSet());
    }

    public UserDTO(User user, Authority eventRole) {
        this(user);
        this.eventRole = eventRole;
    }

}
