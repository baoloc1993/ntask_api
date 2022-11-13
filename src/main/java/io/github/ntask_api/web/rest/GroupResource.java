//package io.github.ntask_api.web.rest;
//
//import io.github.ntask_api.repository.GroupRepository;
//import io.github.ntask_api.web.rest.errors.BadRequestAlertException;
//import java.net.URI;
//import java.net.URISyntaxException;
//import java.util.List;
//import java.util.Objects;
//import java.util.Optional;
//
//import io.github.ntask_api.web.rest.errors.NotFoundException;
//import lombok.extern.slf4j.Slf4j;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.ResponseEntity;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.bind.annotation.*;
//import tech.jhipster.web.util.HeaderUtil;
//import tech.jhipster.web.util.ResponseUtil;
//
///**
// * REST controller for managing {@link io.github.ntask_api.domain.Group}.
// */
//@RestController
//@RequestMapping("/api")
//@Transactional
//@Slf4j
//public class GroupResource {
//
//    private static final String ENTITY_NAME = "groupDto";
//
//    @Value("${jhipster.clientApp.name}")
//    private String applicationName;
//
//    private final GroupRepository groupRepository;
//
//    public GroupResource(GroupRepository groupRepository) {
//        this.groupRepository = groupRepository;
//    }
//
//    /**
//     * {@code POST  /groups} : Create a new groupDto.
//     *
//     * @param groupDto the groupDto to create.
//     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new groupDto, or with status {@code 400 (Bad Request)} if the groupDto has already an ID.
//     * @throws URISyntaxException if the Location URI syntax is incorrect.
//     */
//    @PostMapping("/groups")
//    public ResponseEntity<GroupDTO> createGroup(@RequestBody GroupDTO groupDto) throws URISyntaxException {
//        log.debug("REST request to save Group : {}", groupDto);
//        if (groupDto.getId() != null) {
//            throw new BadRequestAlertException("A new groupDto cannot already have an ID", ENTITY_NAME, "idexists");
//        }
//        GroupDTO result = new GroupDTO(groupRepository.save(new Group(groupDto)));
//        return ResponseEntity
//            .created(new URI("/api/groups/" + result.getId()))
//            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
//            .body(result);
//    }
//
//    /**
//     * {@code PUT  /groups/:id} : Updates an existing groupDto.
//     *
//     * @param id the id of the groupDto to save.
//     * @param groupDto the groupDto to update.
//     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated groupDto,
//     * or with status {@code 400 (Bad Request)} if the groupDto is not valid,
//     * or with status {@code 500 (Internal Server Error)} if the groupDto couldn't be updated.
//     * @throws URISyntaxException if the Location URI syntax is incorrect.
//     */
//    @PutMapping("/groups/{id}")
//    public ResponseEntity<GroupDTO> updateGroup(@PathVariable(value = "id", required = false) final Long id, @RequestBody GroupDTO groupDto)
//        throws URISyntaxException {
//        log.debug("REST request to update Group : {}, {}", id, groupDto);
//        if (groupDto.getId() == null) {
//            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//        }
//        if (!Objects.equals(id, groupDto.getId())) {
//            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
//        }
//
//        if (!groupRepository.existsById(id)) {
//            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
//        }
//
//        GroupDTO result = groupRepository.findById(groupDto.getId()).map(e -> {
//            e.setName(groupDto.getName());
//            return e;
//        }).map(groupRepository::save).map(GroupDTO::new).orElseThrow(NotFoundException::new);
//        return ResponseEntity
//            .ok()
//            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, groupDto.getId().toString()))
//            .body(result);
//    }
//
//    /**
//     * {@code PATCH  /groups/:id} : Partial updates given fields of an existing groupDto, field will ignore if it is null
//     *
//     * @param id the id of the groupDto to save.
//     * @param groupDto the groupDto to update.
//     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated groupDto,
//     * or with status {@code 400 (Bad Request)} if the groupDto is not valid,
//     * or with status {@code 404 (Not Found)} if the groupDto is not found,
//     * or with status {@code 500 (Internal Server Error)} if the groupDto couldn't be updated.
//     * @throws URISyntaxException if the Location URI syntax is incorrect.
//     */
//    @PatchMapping(value = "/groups/{id}", consumes = { "application/json", "application/merge-patch+json" })
//    public ResponseEntity<GroupDTO> partialUpdateGroup(@PathVariable(value = "id", required = false) final Long id, @RequestBody GroupDTO groupDto)
//        throws URISyntaxException {
//        log.debug("REST request to partial update Group partially : {}, {}", id, groupDto);
//        if (groupDto.getId() == null) {
//            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//        }
//        if (!Objects.equals(id, groupDto.getId())) {
//            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
//        }
//
//        if (!groupRepository.existsById(id)) {
//            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
//        }
//
//        Optional<GroupDTO> result = groupRepository
//            .findById(groupDto.getId())
//            .map(existingGroup -> {
//                if (groupDto.getName() != null) {
//                    existingGroup.setName(groupDto.getName());
//                }
//
//                return existingGroup;
//            })
//            .map(groupRepository::save).map(GroupDTO::new);
//
//        return ResponseUtil.wrapOrNotFound(
//            result,
//            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, groupDto.getId().toString())
//        );
//    }
//
//    /**
//     * {@code GET  /groups} : get all the groups.
//     *
//     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of groups in body.
//     */
//    @GetMapping("/groups")
//    public Page<GroupDTO> getAllGroups(Pageable pageable) {
//        log.debug("REST request to get all Groups");
//        return groupRepository.findAll(pageable).map(GroupDTO::new);
//    }
//
//    /**
//     * {@code GET  /groups/:id} : get the "id" groupDto.
//     *
//     * @param id the id of the groupDto to retrieve.
//     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the groupDto, or with status {@code 404 (Not Found)}.
//     */
//    @GetMapping("/groups/{id}")
//    public ResponseEntity<GroupDTO> getGroup(@PathVariable Long id) {
//        log.debug("REST request to get Group : {}", id);
//        Optional<GroupDTO> groupDto = groupRepository.findById(id).map(GroupDTO::new);
//        return ResponseUtil.wrapOrNotFound(groupDto);
//    }
//
//    /**
//     * {@code DELETE  /groups/:id} : delete the "id" groupDto.
//     *
//     * @param id the id of the groupDto to delete.
//     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
//     */
//    @DeleteMapping("/groups/{id}")
//    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
//        log.debug("REST request to delete Group : {}", id);
//        groupRepository.deleteById(id);
//        return ResponseEntity
//            .noContent()
//            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
//            .build();
//    }
//}
