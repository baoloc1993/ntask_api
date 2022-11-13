package io.github.ntask_api.web.rest;

import io.github.ntask_api.service.UserService;
import io.github.ntask_api.service.dto.EventDTO;
import io.github.ntask_api.service.dto.TaskDTO;
import io.github.ntask_api.service.dto.UserDTO;
import java.util.*;
import java.util.Collections;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

@RestController
@RequestMapping("/api")
@Slf4j
public class PublicUserResource {

    private static final List<String> ALLOWED_ORDERED_PROPERTIES = Collections.unmodifiableList(
        Arrays.asList("id", "login", "firstName", "lastName", "email", "activated", "langKey")
    );

    private final UserService userService;

    public PublicUserResource(UserService userService) {
        this.userService = userService;
    }

    /**
     * {@code GET /users} : get all users with only the public informations - calling this are allowed for anyone.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body all users.
     */
    @GetMapping("/users")
    public ResponseEntity<Page<UserDTO>> getAllPublicUsers(String q, Pageable pageable) {
        log.debug("REST request to get all public User names");
        if (!onlyContainsAllowedProperties(pageable)) {
            return ResponseEntity.badRequest().build();
        }

        final Page<UserDTO> page = userService.getAllPublicUsers(q, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page, headers, HttpStatus.OK);
    }

    private boolean onlyContainsAllowedProperties(Pageable pageable) {
        return pageable.getSort().stream().map(Sort.Order::getProperty).allMatch(ALLOWED_ORDERED_PROPERTIES::contains);
    }

    @GetMapping("/users/{id}")
    public UserDTO findById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @GetMapping("/users/{uid}/events/{eid}/tasks")
    public Page<TaskDTO> findTasksBy(@PathVariable("uid") Long uid, @PathVariable("eid") Long eid, Pageable pageable) {
        return userService.findTasksBy(uid, eid, pageable);
    }

    @GetMapping("/users/{uid}/events")
    public Page<EventDTO> findEventsBy(@PathVariable("uid") Long uid, Pageable pageable) {
        return userService.findEventsBy(uid, pageable);
    }

    @PostMapping("/users/{uid}/events/bookmarks")
    public UserDTO bookmarkEvents(@PathVariable("uid") Long uid, @RequestBody List<Long> eventIds) {
        return userService.bookmarkEvents(uid, eventIds);
    }

    @GetMapping("/users/{uid}/events/bookmarks")
    public List<EventDTO> getBookmarkEvents(@PathVariable("uid") Long uid) {
        return userService.getBookmarkEvents(uid);
    }

    /**
     * Gets a list of all roles.
     * @return a string list of all roles.
     */
    @GetMapping("/authorities")
    public List<String> getAuthorities() {
        return userService.getAuthorities();
    }
}
