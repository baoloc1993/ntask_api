//package io.github.ntask_api.web.rest;
//
//import io.github.ntask_api.domain.GroupEvent;
//import io.github.ntask_api.repository.GroupEventRepository;
//import io.github.ntask_api.service.dto.GroupEventDTO;
//import io.github.ntask_api.web.rest.errors.BadRequestAlertException;
//import java.net.URI;
//import java.net.URISyntaxException;
//import java.util.List;
//import java.util.Objects;
//import java.util.Optional;
//
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
// * REST controller for managing {@link io.github.ntask_api.domain.GroupEvent}.
// */
//@RestController
//@RequestMapping("/api")
//@Transactional
//@Slf4j
//public class GroupEventResource {
//
//    private static final String ENTITY_NAME = "groupEventDto";
//
//    @Value("${jhipster.clientApp.name}")
//    private String applicationName;
//
//    private final GroupEventRepository groupEventRepository;
//
//    public GroupEventResource(GroupEventRepository groupEventRepository) {
//        this.groupEventRepository = groupEventRepository;
//    }
//
//    /**
//     * {@code POST  /group-events} : Create a new groupEventDto.
//     *
//     * @param groupEventDto the groupEventDto to create.
//     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new groupEventDto, or with status {@code 400 (Bad Request)} if the groupEventDto has already an ID.
//     * @throws URISyntaxException if the Location URI syntax is incorrect.
//     */
//    @PostMapping("/group-events")
//    public ResponseEntity<GroupEventDTO> createGroupEvent(@RequestBody GroupEventDTO groupEventDto) throws URISyntaxException {
//        log.debug("REST request to save GroupEvent : {}", groupEventDto);
//        if (groupEventDto.getId() != null) {
//            throw new BadRequestAlertException("A new groupEventDto cannot already have an ID", ENTITY_NAME, "idexists");
//        }
//        GroupEventDTO result = new GroupEventDTO(groupEventRepository.save(new GroupEvent(groupEventDto)));
//        return ResponseEntity
//            .created(new URI("/api/group-events/" + result.getId()))
//            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
//            .body(result);
//    }
//
////    /**
////     * {@code PUT  /group-events/:id} : Updates an existing groupEventDto.
////     *
////     * @param id the id of the groupEventDto to save.
////     * @param groupEventDto the groupEventDto to update.
////     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated groupEventDto,
////     * or with status {@code 400 (Bad Request)} if the groupEventDto is not valid,
////     * or with status {@code 500 (Internal Server Error)} if the groupEventDto couldn't be updated.
////     * @throws URISyntaxException if the Location URI syntax is incorrect.
////     */
////    @PutMapping("/group-events/{id}")
////    public ResponseEntity<GroupEventDTO> updateGroupEvent(
////        @PathVariable(value = "id", required = false) final Long id,
////        @RequestBody GroupEventDTO groupEventDto
////    ) throws URISyntaxException {
////        log.debug("REST request to update GroupEvent : {}, {}", id, groupEventDto);
////        if (groupEventDto.getId() == null) {
////            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
////        }
////        if (!Objects.equals(id, groupEventDto.getId())) {
////            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
////        }
////
////        if (!groupEventRepository.existsById(id)) {
////            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
////        }
////
////        GroupEventDTO result = groupEventRepository.findById(groupEventDto.getId()).map(e -> {
//////            e.set
////        });
////        return ResponseEntity
////            .ok()
////            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, groupEventDto.getId().toString()))
////            .body(result);
////    }
////
////    /**
////     * {@code PATCH  /group-events/:id} : Partial updates given fields of an existing groupEventDto, field will ignore if it is null
////     *
////     * @param id the id of the groupEventDto to save.
////     * @param groupEventDto the groupEventDto to update.
////     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated groupEventDto,
////     * or with status {@code 400 (Bad Request)} if the groupEventDto is not valid,
////     * or with status {@code 404 (Not Found)} if the groupEventDto is not found,
////     * or with status {@code 500 (Internal Server Error)} if the groupEventDto couldn't be updated.
////     * @throws URISyntaxException if the Location URI syntax is incorrect.
////     */
////    @PatchMapping(value = "/group-events/{id}", consumes = { "application/json", "application/merge-patch+json" })
////    public ResponseEntity<GroupEventDTO> partialUpdateGroupEvent(
////        @PathVariable(value = "id", required = false) final Long id,
////        @RequestBody GroupEventDTO groupEventDto
////    ) throws URISyntaxException {
////        log.debug("REST request to partial update GroupEvent partially : {}, {}", id, groupEventDto);
////        if (groupEventDto.getId() == null) {
////            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
////        }
////        if (!Objects.equals(id, groupEventDto.getId())) {
////            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
////        }
////
////        if (!groupEventRepository.existsById(id)) {
////            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
////        }
////
////        Optional<GroupEvent> result = groupEventRepository
////            .findById(groupEventDto.getId())
////            .map(existingGroupEvent -> {
////                return existingGroupEvent;
////            })
////            .map(groupEventRepository::save);
////
////        return ResponseUtil.wrapOrNotFound(
////            result,
////            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, groupEventDto.getId().toString())
////        );
////    }
//
//    /**
//     * {@code GET  /group-events} : get all the groupEvents.
//     *
//     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of groupEvents in body.
//     */
//    @GetMapping("/group-events")
//    public Page<GroupEventDTO> getAllGroupEvents(Pageable pa) {
//        log.debug("REST request to get all GroupEvents");
//        return groupEventRepository.findAll(pa).map(GroupEventDTO::new);
//    }
//
//    /**
//     * {@code GET  /group-events/:id} : get the "id" groupEventDto.
//     *
//     * @param id the id of the groupEventDto to retrieve.
//     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the groupEventDto, or with status {@code 404 (Not Found)}.
//     */
//    @GetMapping("/group-events/{id}")
//    public ResponseEntity<GroupEventDTO> getGroupEvent(@PathVariable Long id) {
//        log.debug("REST request to get GroupEvent : {}", id);
//        Optional<GroupEventDTO> groupEventDto = groupEventRepository.findById(id).map(GroupEventDTO::new);
//        return ResponseUtil.wrapOrNotFound(groupEventDto);
//    }
//
//    /**
//     * {@code DELETE  /group-events/:id} : delete the "id" groupEventDto.
//     *
//     * @param id the id of the groupEventDto to delete.
//     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
//     */
//    @DeleteMapping("/group-events/{id}")
//    public ResponseEntity<Void> deleteGroupEvent(@PathVariable Long id) {
//        log.debug("REST request to delete GroupEvent : {}", id);
//        groupEventRepository.deleteById(id);
//        return ResponseEntity
//            .noContent()
//            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
//            .build();
//    }
//}
