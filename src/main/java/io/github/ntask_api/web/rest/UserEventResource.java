package io.github.ntask_api.web.rest;

import io.github.ntask_api.domain.Authority;
import io.github.ntask_api.domain.UserEvent;
import io.github.ntask_api.repository.UserEventRepository;
import io.github.ntask_api.service.dto.UserEventDTO;
import io.github.ntask_api.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link UserEvent}.
 */
@RestController
@RequestMapping("/api")
@Transactional
@Slf4j
public class UserEventResource {

    private static final String ENTITY_NAME = "userGroup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserEventRepository userEventRepository;

    public UserEventResource(UserEventRepository userEventRepository) {
        this.userEventRepository = userEventRepository;
    }

    /**
     * {@code POST  /user-groups} : Create a new userGroup.
     *
     * @param userEventDto the userGroup to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userGroup, or with status {@code 400 (Bad Request)} if the userGroup has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-groups")
    public ResponseEntity<UserEventDTO> createUserGroup(@RequestBody UserEventDTO userEventDto) throws URISyntaxException {
        log.debug("REST request to save UserGroup : {}", userEventDto);
        if (userEventDto.getId() != null) {
            throw new BadRequestAlertException("A new userGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserEventDTO result = new UserEventDTO(userEventRepository.save(new UserEvent(userEventDto)));
        return ResponseEntity
            .created(new URI("/api/user-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

//    /**
//     * {@code PUT  /user-groups/:id} : Updates an existing userGroup.
//     *
//     * @param id the id of the userGroup to save.
//     * @param userGroupDto the userGroup to update.
//     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userGroup,
//     * or with status {@code 400 (Bad Request)} if the userGroup is not valid,
//     * or with status {@code 500 (Internal Server Error)} if the userGroup couldn't be updated.
//     * @throws URISyntaxException if the Location URI syntax is incorrect.
//     */
//    @PutMapping("/user-groups/{id}")
//    public ResponseEntity<UserGroupDTO> updateUserGroup(
//        @PathVariable(value = "id", required = false) final Long id,
//        @RequestBody UserGroupDto userGroupDto
//    ) throws URISyntaxException {
//        log.debug("REST request to update UserGroup : {}, {}", id, userGroup);
//        if (userGroupDto.getId() == null) {
//            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//        }
//        if (!Objects.equals(id, userGroupDto.getId())) {
//            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
//        }
//
//        if (!userGroupRepository.existsById(id)) {
//            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
//        }
//
//        UserGroup result = userGroupRepository.findById(userGroupDto.getId()).orElseThrow(NotFoundException::new);
//        result.set
//        return ResponseEntity
//            .ok()
//            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userGroup.getId().toString()))
//            .body(result);
//    }

    /**
     * {@code PATCH  /user-groups/:id} : Partial updates given fields of an existing userGroup, field will ignore if it is null
     *
     * @param id the id of the userGroup to save.
     * @param userEventDto the userGroup to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userGroup,
     * or with status {@code 400 (Bad Request)} if the userGroup is not valid,
     * or with status {@code 404 (Not Found)} if the userGroup is not found,
     * or with status {@code 500 (Internal Server Error)} if the userGroup couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/user-groups/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserEventDTO> partialUpdateUserGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserEventDTO userEventDto
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserGroup partially : {}, {}", id, userEventDto);
        if (userEventDto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userEventDto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userEventRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserEventDTO> result = userEventRepository
            .findById(userEventDto.getId())
            .map(existingUserGroup -> {
                if (userEventDto.getRole() != null) {
                    existingUserGroup.setRole(new Authority(userEventDto.getRole()));
                }

                return existingUserGroup;
            })
            .map(userEventRepository::save).map(UserEventDTO::new);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userEventDto.getId().toString())
        );
    }

    /**
     * {@code GET  /user-groups} : get all the userGroups.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userGroups in body.
     */
    @GetMapping("/user-groups")
    public Page<UserEventDTO> getAllUserGroups(Pageable pageable) {
        log.debug("REST request to get all UserGroups");
        return userEventRepository.findAll(pageable).map(UserEventDTO::new);
    }

    /**
     * {@code GET  /user-groups/:id} : get the "id" userGroup.
     *
     * @param id the id of the userGroup to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userGroup, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-groups/{id}")
    public ResponseEntity<UserEventDTO> getUserGroup(@PathVariable Long id) {
        log.debug("REST request to get UserGroup : {}", id);
        Optional<UserEventDTO> userGroup = userEventRepository.findById(id).map(UserEventDTO::new);
        return ResponseUtil.wrapOrNotFound(userGroup);
    }

    /**
     * {@code DELETE  /user-groups/:id} : delete the "id" userGroup.
     *
     * @param id the id of the userGroup to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-groups/{id}")
    public ResponseEntity<Void> deleteUserGroup(@PathVariable Long id) {
        log.debug("REST request to delete UserGroup : {}", id);
        userEventRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
