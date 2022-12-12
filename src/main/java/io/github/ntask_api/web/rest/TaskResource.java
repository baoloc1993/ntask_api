package io.github.ntask_api.web.rest;

import com.google.type.DateTime;
import io.github.ntask_api.domain.*;
import io.github.ntask_api.repository.NotificationTaskRepository;
import io.github.ntask_api.repository.TaskRepository;
import io.github.ntask_api.repository.UserRepository;
import io.github.ntask_api.repository.UserTaskRepository;
import io.github.ntask_api.security.SecurityUtils;
import io.github.ntask_api.service.NotificationService;
import io.github.ntask_api.service.UserService;
import io.github.ntask_api.service.dto.Notice;
import io.github.ntask_api.service.dto.TaskDTO;
import io.github.ntask_api.service.dto.UserDTO;
import io.github.ntask_api.web.rest.errors.BadRequestAlertException;
import io.github.ntask_api.web.rest.errors.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * REST controller for managing {@link io.github.ntask_api.domain.Task}.
 */
@RestController
@RequestMapping("/api")
@Transactional
@Slf4j
@RequiredArgsConstructor
public class TaskResource {

    private static final String ENTITY_NAME = "taskDto";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final UserTaskRepository userTaskRepository;
    private final NotificationService notificationService;
    private final NotificationTaskRepository notificationTaskRepository;
    private final UserService userService;

    /**
     * {@code POST  /tasks} : Create a new taskDto.
     *
     * @param taskDto the taskDto to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new taskDto, or with status {@code 400 (Bad Request)} if the taskDto has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tasks")
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO taskDto) throws URISyntaxException {
        log.debug("REST request to save Task : {}", taskDto);
        if (taskDto.getId() != null) {
            throw new BadRequestAlertException("A new taskDto cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Task entity = new Task(taskDto);

        Set<UserTask> userTasks = new HashSet<>();
//        userTasks.add(new UserTask(null, entity, userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().orElseThrow()).orElseThrow()));
        if (taskDto.getAssignees() != null) {
            userTasks.addAll(userRepository.findAllById(taskDto.getAssignees().stream().map(UserDTO::getId).collect(Collectors.toList()))
                    .stream().map(u -> new UserTask(null, entity, u)).collect(Collectors.toSet()));
        }
        userTaskRepository.saveAll(userTasks);

        TaskDTO result = new TaskDTO(taskRepository.save(entity));


        NotificationTask notificationTask = new NotificationTask();
        User user = userService.getUserWithAuthorities().get();
        notificationTask.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        notificationTask.setReaded(false);
        notificationTask.setDeleted(false);
        notificationTask.setUser(user);
        notificationTask.setType(1);
        notificationTask.setContent("Tạo task: "+entity.getName()+" thành công");
        notificationTask.setTask(entity);
        notificationTaskRepository.save(notificationTask);


        return ResponseEntity
                .created(new URI("/api/tasks/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /tasks/:id} : Updates an existing taskDto.
     *
     * @param id      the id of the taskDto to save.
     * @param taskDto the taskDto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated taskDto,
     * or with status {@code 400 (Bad Request)} if the taskDto is not valid,
     * or with status {@code 500 (Internal Server Error)} if the taskDto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tasks/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable(value = "id", required = false) final Long id, @RequestBody TaskDTO taskDto)
            throws URISyntaxException {
        log.debug("REST request to update Task : {}, {}", id, taskDto);
        if (taskDto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, taskDto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!taskRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TaskDTO result = taskRepository.findById(taskDto.getId()).map(e -> {
            e.setStartAt(taskDto.getStartAt());
            e.setEndAt(taskDto.getEndAt());
            e.setDescription(taskDto.getDescription());
            return e;
        }).map(taskRepository::save).map(TaskDTO::new).orElseThrow(NotFoundException::new);




        return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, taskDto.getId().toString()))
                .body(result);
    }

    /**
     * {@code PATCH  /tasks/:id} : Partial updates given fields of an existing taskDto, field will ignore if it is null
     *
     * @param id      the id of the taskDto to save.
     * @param taskDto the taskDto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated taskDto,
     * or with status {@code 400 (Bad Request)} if the taskDto is not valid,
     * or with status {@code 404 (Not Found)} if the taskDto is not found,
     * or with status {@code 500 (Internal Server Error)} if the taskDto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tasks/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public ResponseEntity<TaskDTO> partialUpdateTask(@PathVariable(value = "id", required = false) final Long id,
                                                     @RequestParam(value = "isUpdateMember", required = false) Boolean isUpdateMember,
                                                     @RequestHeader(value = "registrationToken") String registrationToken,
                                                     @RequestBody TaskDTO taskDto)
            throws URISyntaxException {
        log.debug("REST request to partial update Task partially : {}, {}", id, taskDto);
        if (taskDto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, taskDto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!taskRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TaskDTO> result = taskRepository
                .findById(taskDto.getId())
                .map(existingTask -> {
                    if (taskDto.getStartAt() != null) {
                        existingTask.setStartAt(taskDto.getStartAt());
                    }
                    if (taskDto.getEndAt() != null) {
                        existingTask.setEndAt(taskDto.getEndAt());
                    }
                    if (taskDto.getDescription() != null) {
                        existingTask.setDescription(taskDto.getDescription());
                    }
                    if (taskDto.getAssignees() != null) {
                        Set<UserTask> userTasks = userRepository
                                .findAllById(
                                        taskDto
                                                .getAssignees()
                                                .stream().map(UserDTO::getId)
                                                .collect(Collectors.toList())
                                )
                                .stream()
                                .map(u -> new UserTask(null, existingTask, u))
                                .collect(Collectors.toSet());
                        if (Boolean.TRUE.equals(isUpdateMember)){
                            Notice notice = new Notice();
                            notice.setContent("Bạn vừa được giao việc " + taskDto.getName());
                            notice.setSubject("Công việc " + taskDto.getName() + " đã được cập nhật");
                            Map<String,String> data = new HashMap<>();
                            data.put("id", String.valueOf(id));
                            data.put("type", "3");
                            notice.setData(data);
                            notice.setRegistrationTokens(userTasks.stream().map(userTask -> userTask.getUser().getNotificationKey()).collect(Collectors.toList()));
                            notificationService.sendNotification(notice);
                        }

                        existingTask.setUserTask(userTasks);

                    }
                    if (taskDto.getName() != null) {
                        existingTask.setName(taskDto.getName());
                    }
                    if (taskDto.getStatus() != null) {
                        existingTask.setStatus(Status.valueOf(taskDto.getStatus()));
                    }

                    return existingTask;
                })
                .map(taskRepository::save).map(TaskDTO::new);
        NotificationTask notificationTask = new NotificationTask();
        notificationTask.setTask(taskRepository.findById(taskDto.getId()).get());
        notificationTask.setContent("Cập nhật task: "+taskDto.getName()+" thành công");
        notificationTask.setDeleted(false);
        notificationTask.setType(2);
        notificationTask.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        notificationTask.setReaded(false);
        notificationTask.setUser(userService.getUserWithAuthorities().get());
        notificationTaskRepository.save(notificationTask);
        return ResponseUtil.wrapOrNotFound(
                result,
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, taskDto.getId().toString())
        );
    }

    /**
     * {@code GET  /tasks} : get all the tasks.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tasks in body.
     */
    @GetMapping("/tasks")
    public Page<TaskDTO> getAllTasks(Pageable pa) {
        log.debug("REST request to get all Tasks");
        return taskRepository.findAll(pa).map(TaskDTO::new);
    }

    /**
     * {@code GET  /tasks/:id} : get the "id" taskDto.
     *
     * @param id the id of the taskDto to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the taskDto, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tasks/{id}")
    public ResponseEntity<TaskDTO> getTask(@PathVariable Long id) {
        log.debug("REST request to get Task : {}", id);
        Optional<TaskDTO> taskDto = taskRepository.findById(id).map(TaskDTO::new);
        return ResponseUtil.wrapOrNotFound(taskDto);
    }

    /**
     * {@code DELETE  /tasks/:id} : delete the "id" taskDto.
     *
     * @param id the id of the taskDto to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        log.debug("REST request to delete Task : {}", id);


        List<NotificationTask> list = notificationTaskRepository.findByTaskId(id);
        Task task = taskRepository.findById(id).get();
        for(NotificationTask n : list){
            n.setTask(null);
            n.setDeleted(true);
            notificationTaskRepository.save(n);
        }
        NotificationTask notificationTask = new NotificationTask();
        notificationTask.setTask(null);
        notificationTask.setContent("Xóa task: "+task.getName()+" thành công");
        notificationTask.setDeleted(true);
        notificationTask.setType(3);
        notificationTask.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        notificationTask.setReaded(false);
        notificationTask.setUser(userService.getUserWithAuthorities().get());
        notificationTaskRepository.save(notificationTask);

        taskRepository.deleteById(id);
        return ResponseEntity
                .noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                .build();
    }

    @GetMapping("/task/findByEvent")
    public Page<TaskDTO> findByEvent(Pageable pageable,@RequestParam("id") Long id){
        return taskRepository.findByEventId(id,pageable).map(TaskDTO::new);
    }

    @GetMapping("/notificationTask/findByUser")
    public Page<NotificationTask> findByEvent(Pageable pageable){
        return notificationTaskRepository.findByUserId(userService.getUserWithAuthorities().get().getId(), pageable);
    }

    @GetMapping("/notificationTask/readAll")
    public void updateReadNotification(){
        User user = userService.getUserWithAuthorities().get();
        for(NotificationTask n : notificationTaskRepository.findListByUserId(user.getId())){
            n.setReaded(true);
            notificationTaskRepository.save(n);
        }
    }

    @GetMapping("/notificationTask/notRead")
    public Long getNotificationNotRead(){
        User user = userService.getUserWithAuthorities().get();
        return notificationTaskRepository.countNotificationNotRead(user.getId());
    }
}
