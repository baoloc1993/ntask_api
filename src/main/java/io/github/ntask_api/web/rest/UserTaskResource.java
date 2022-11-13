package io.github.ntask_api.web.rest;

import io.github.ntask_api.domain.UserTask;
import io.github.ntask_api.repository.UserTaskRepository;
import io.github.ntask_api.service.dto.UserTaskDTO;
import io.github.ntask_api.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link io.github.ntask_api.domain.UserTask}.
 */
@RestController
@RequestMapping("/api")
@Transactional
@Slf4j
public class UserTaskResource {

    private static final String ENTITY_NAME = "userTaskDto";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserTaskRepository userTaskRepository;

    public UserTaskResource(UserTaskRepository userTaskRepository) {
        this.userTaskRepository = userTaskRepository;
    }

    /**
     * {@code POST  /user-tasks} : Create a new userTaskDto.
     *
     * @param userTaskDto the userTaskDto to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userTaskDto, or with status {@code 400 (Bad Request)} if the userTaskDto has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-tasks")
    public ResponseEntity<UserTaskDTO> createUserTask(@RequestBody UserTaskDTO userTaskDto) throws URISyntaxException {
        log.debug("REST request to save UserTask : {}", userTaskDto);
        if (userTaskDto.getId() != null) {
            throw new BadRequestAlertException("A new userTaskDto cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserTaskDTO result = new UserTaskDTO(userTaskRepository.save(new UserTask(userTaskDto)));
        return ResponseEntity
            .created(new URI("/api/user-tasks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

//    /**
//     * {@code PUT  /user-tasks/:id} : Updates an existing userTaskDto.
//     *
//     * @param id the id of the userTaskDto to save.
//     * @param userTaskDto the userTaskDto to update.
//     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userTaskDto,
//     * or with status {@code 400 (Bad Request)} if the userTaskDto is not valid,
//     * or with status {@code 500 (Internal Server Error)} if the userTaskDto couldn't be updated.
//     * @throws URISyntaxException if the Location URI syntax is incorrect.
//     */
//    @PutMapping("/user-tasks/{id}")
//    public ResponseEntity<UserTaskDTO> updateUserTask(
//        @PathVariable(value = "id", required = false) final Long id,
//        @RequestBody UserTaskDTO userTaskDto
//    ) throws URISyntaxException {
//        log.debug("REST request to update UserTask : {}, {}", id, userTaskDto);
//        if (userTaskDto.getId() == null) {
//            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//        }
//        if (!Objects.equals(id, userTaskDto.getId())) {
//            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
//        }
//
//        if (!userTaskRepository.existsById(id)) {
//            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
//        }
//
//        UserTaskDTO result = userTaskRepository.findById(userTaskDto.getId()).map(e -> {
//            e.set
//        });
//        return ResponseEntity
//            .ok()
//            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userTaskDto.getId().toString()))
//            .body(result);
//    }
//
//    /**
//     * {@code PATCH  /user-tasks/:id} : Partial updates given fields of an existing userTaskDto, field will ignore if it is null
//     *
//     * @param id the id of the userTaskDto to save.
//     * @param userTaskDto the userTaskDto to update.
//     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userTaskDto,
//     * or with status {@code 400 (Bad Request)} if the userTaskDto is not valid,
//     * or with status {@code 404 (Not Found)} if the userTaskDto is not found,
//     * or with status {@code 500 (Internal Server Error)} if the userTaskDto couldn't be updated.
//     * @throws URISyntaxException if the Location URI syntax is incorrect.
//     */
//    @PatchMapping(value = "/user-tasks/{id}", consumes = { "application/json", "application/merge-patch+json" })
//    public ResponseEntity<UserTaskDTO> partialUpdateUserTask(
//        @PathVariable(value = "id", required = false) final Long id,
//        @RequestBody UserTaskDTO userTaskDto
//    ) throws URISyntaxException {
//        log.debug("REST request to partial update UserTask partially : {}, {}", id, userTaskDto);
//        if (userTaskDto.getId() == null) {
//            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//        }
//        if (!Objects.equals(id, userTaskDto.getId())) {
//            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
//        }
//
//        if (!userTaskRepository.existsById(id)) {
//            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
//        }
//
//        Optional<UserTask> result = userTaskRepository
//            .findById(userTaskDto.getId())
//            .map(existingUserTask -> {
//                return existingUserTask;
//            })
//            .map(userTaskRepository::save);
//
//        return ResponseUtil.wrapOrNotFound(
//            result,
//            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userTaskDto.getId().toString())
//        );
//    }

    /**
     * {@code GET  /user-tasks} : get all the userTasks.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userTasks in body.
     */
    @GetMapping("/user-tasks")
    public Page<UserTaskDTO> getAllUserTasks(Pageable pageable) {
        log.debug("REST request to get all UserTasks");
        return userTaskRepository.findAll(pageable).map(UserTaskDTO::new);
    }

    /**
     * {@code GET  /user-tasks/:id} : get the "id" userTaskDto.
     *
     * @param id the id of the userTaskDto to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userTaskDto, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-tasks/{id}")
    public ResponseEntity<UserTaskDTO> getUserTask(@PathVariable Long id) {
        log.debug("REST request to get UserTask : {}", id);
        Optional<UserTaskDTO> userTaskDto = userTaskRepository.findById(id).map(UserTaskDTO::new);
        return ResponseUtil.wrapOrNotFound(userTaskDto);
    }

    /**
     * {@code DELETE  /user-tasks/:id} : delete the "id" userTaskDto.
     *
     * @param id the id of the userTaskDto to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-tasks/{id}")
    public ResponseEntity<Void> deleteUserTask(@PathVariable Long id) {
        log.debug("REST request to delete UserTask : {}", id);
        userTaskRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
