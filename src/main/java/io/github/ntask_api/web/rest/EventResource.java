package io.github.ntask_api.web.rest;

import com.google.type.DateTime;
import io.github.ntask_api.domain.*;
import io.github.ntask_api.repository.*;
import io.github.ntask_api.security.AuthoritiesConstants;
import io.github.ntask_api.security.SecurityUtils;
import io.github.ntask_api.service.NotificationService;
import io.github.ntask_api.service.UserService;
import io.github.ntask_api.service.dto.*;
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
import org.springframework.security.core.context.SecurityContextHolder;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * REST controller for managing {@link io.github.ntask_api.domain.Event}.
 */
@RestController
@RequestMapping("/api")
@Transactional
@Slf4j
@RequiredArgsConstructor
public class EventResource {

    private static final String ENTITY_NAME = "eventDto";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AuthorityRepository authorityRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final UserEventRepository userEventRepository;
    private final MessageRepository messageRepository;
    private final NotificationService notificationService;
    private final UserService userService;
    private final NotificationEventRepository notificationEventRepository;
    private final NotificationTaskRepository notificationTaskRepository;



    /**
     * {@code POST  /events} : Create a new eventDto.
     *
     * @param eventDto the eventDto to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new eventDto, or with status {@code 400 (Bad Request)} if the eventDto has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/events")
    public ResponseEntity<EventDTO> createEvent(@RequestBody EventDTO eventDto) throws URISyntaxException {
        log.debug("REST request to save Event : {}", eventDto);
        if (eventDto.getId() != null) {
            throw new BadRequestAlertException("A new eventDto cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Event entity = eventRepository.save(new Event(eventDto));
        EventDTO result = new EventDTO(entity);

        Set<UserEvent> ug = new HashSet<>();
        ug.add(new UserEvent(authorityRepository.findById(AuthoritiesConstants.ADMIN_ID).orElseThrow(),
                userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().orElseThrow()).orElseThrow(), entity));
        if(eventDto.getMembers() != null) {

            Set<UserEvent> as = eventDto.getMembers().stream()
                    .map(ud -> {
                        Authority authority = authorityRepository.findByName(ud.getEventRole().getName()).orElse(null);
                        return new UserEvent(authority, new User(ud.getId()), entity);
                    }).collect(Collectors.toSet());
            ug.addAll(as);
        }
        userEventRepository.saveAll(ug);

        NotificationEvent notificationEvent = new NotificationEvent();
        User user = userService.getUserWithAuthorities().get();
        notificationEvent.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        notificationEvent.setReaded(false);
        notificationEvent.setDeleted(false);
        notificationEvent.setUser(user);
        notificationEvent.setType(4);
        notificationEvent.setContent("Tạo event: "+entity.getName()+" thành công");
        notificationEvent.setEvent(entity);
        notificationEventRepository.save(notificationEvent);


        return ResponseEntity
            .created(new URI("/api/events/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /events/:id} : Updates an existing eventDto.
     *
     * @param id the id of the eventDto to save.
     * @param eventDto the eventDto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventDto,
     * or with status {@code 400 (Bad Request)} if the eventDto is not valid,
     * or with status {@code 500 (Internal Server Error)} if the eventDto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/events/{id}")
    public ResponseEntity<EventDTO> updateEvent(@PathVariable(value = "id", required = false) final Long id, @RequestBody EventDTO eventDto)
        throws URISyntaxException {
        log.debug("REST request to update Event : {}, {}", id, eventDto);
        if (eventDto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventDto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EventDTO result = eventRepository.findById(eventDto.getId()). map(e -> {
            e.setName(eventDto.getName());
            return e;
        }).map(eventRepository::save).map(EventDTO::new).orElseThrow(NotFoundException::new);

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventDto.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /events/:id} : Partial updates given fields of an existing eventDto, field will ignore if it is null
     *
     * @param id the id of the eventDto to save.
     * @param eventDto the eventDto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventDto,
     * or with status {@code 400 (Bad Request)} if the eventDto is not valid,
     * or with status {@code 404 (Not Found)} if the eventDto is not found,
     * or with status {@code 500 (Internal Server Error)} if the eventDto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/events/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EventDTO> partialUpdateEvent(@PathVariable(value = "id", required = false) final Long id,
                                                       @Header(value = "registrationToken") final String registrationToken,
                                                       @RequestBody EventDTO eventDto)
        throws URISyntaxException {
        log.debug("REST request to partial update Event partially : {}, {}", id, eventDto);
        if (eventDto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventDto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EventDTO> result = eventRepository
            .findById(eventDto.getId())
            .map(existingEvent -> {
                if (eventDto.getName() != null) {
                    existingEvent.setName(eventDto.getName());
                }
                if (eventDto.getStatus() != null) {
                    existingEvent.setStatus(Status.valueOf(eventDto.getStatus()));
                }
                if(eventDto.getExp() != null) {
                    existingEvent.setExp(eventDto.getExp());
                }
                if (eventDto.getDescription() != null) {
                    existingEvent.setDescription(eventDto.getDescription());
                }
                if (eventDto.getStartAt() != null) {
                    existingEvent.setStartAt(eventDto.getStartAt());
                }
                if (eventDto.getEndAt() != null) {
                    existingEvent.setEndAt(eventDto.getEndAt());
                }
                if (eventDto.getMembers() != null) {
                    Authority authority = authorityRepository.findById(AuthoritiesConstants.USER_ID).orElseThrow();
                    Set<Long> userIds = userEventRepository.findAllByEventId(existingEvent.getId()).stream().map(userEvent -> userEvent.getUser().getId()).collect(Collectors.toSet());
                    userEventRepository.deleteAllByEventId(existingEvent.getId());
                    Set<UserEvent> ug = eventDto.getMembers().stream()
                            .map(ud -> new UserEvent(authority, new User(ud.getId()), existingEvent)).collect(Collectors.toSet());
                    String a = eventDto.getMembers().stream().map(userDTO -> String.valueOf(userDTO.getId())).sorted().collect(Collectors.joining("-"));
                    String b = userIds.stream().map(String::valueOf).sorted().collect(Collectors.joining("-"));
                    if (!a.equals(b)){
                        Notice notice = new Notice();
                        notice.setContent("Event " + existingEvent.getName() + " updated");
                        notice.setSubject("Event " + existingEvent.getName() + " updated");
                        notice.setRegistrationTokens(List.of(registrationToken));
                        Map<String,String> data = new HashMap<>();
                        data.put("id", String.valueOf(existingEvent.getId()));
                        data.put("createdAt", DateTime.getDefaultInstance().toString());
                        notice.setData(data);
                        notificationService.sendNotification(notice);
                    }
                    ug.add(new UserEvent(authorityRepository.findById(AuthoritiesConstants.ADMIN_ID).orElseThrow(),
                            userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().orElseThrow()).orElseThrow(), existingEvent));

                    userEventRepository.saveAll(ug);
                }

                return existingEvent;
            })
            .map(eventRepository::saveAndFlush).flatMap(e -> eventRepository.findById(eventDto.getId())).map(EventDTO::new);

        Event event = eventRepository.findById(eventDto.getId()).get();
        NotificationEvent notificationEvent = new NotificationEvent();
        User user = userService.getUserWithAuthorities().get();
        notificationEvent.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        notificationEvent.setReaded(false);
        notificationEvent.setDeleted(false);
        notificationEvent.setUser(user);
        notificationEvent.setType(5);
        notificationEvent.setContent("Cập nhật event: "+event.getName()+" thành công");
        notificationEvent.setEvent(event);
        notificationEventRepository.save(notificationEvent);


        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventDto.getId().toString())
        );
    }

    /**
     * {@code GET  /events} : get all the events.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of events in body.
     */
    @GetMapping("/events")
    public Page<EventDTO> getAllEvents(Pageable pa) {
        log.debug("REST request to get all Events");
        return eventRepository.findAll(pa).map(EventDTO::new);
    }

    /**
     * {@code GET  /events/:id} : get the "id" eventDto.
     *
     * @param id the id of the eventDto to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eventDto, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/events/{id}")
    public ResponseEntity<EventDTO> getEvent(@PathVariable Long id) {
        log.debug("REST request to get Event : {}", id);
        Optional<EventDTO> eventDto = eventRepository.findById(id).map(EventDTO::new);
        for(Long e : eventDto.get().getTask()){
            Task t = taskRepository.findById(e).get();
            eventDto.get().getTasks().add(new TaskDTO(t));
        }
        return ResponseUtil.wrapOrNotFound(eventDto);
    }

    @GetMapping("/events/user")
    public Page<EventDTO> getEventRelUser(Pageable pa) {
        log.debug("REST request to get Event relation with current user login");
        List<Long> eventIDs =  userEventRepository
                .findByUserIsCurrentUser()
                .stream().map(UserEvent::getEvent)
                .map(Event::getId)
                .collect(Collectors.toList());
        return eventRepository
                .findAllByIDs(eventIDs, pa)
                .map(EventDTO::new);
    }

    @GetMapping("/events/{id}/tasks")
    public Page<TaskDTO> getAllTasksOf(@PathVariable Long id, Pageable pa) {
        String username = SecurityUtils.getCurrentUserLogin().orElseThrow();
        Set<String> auths = userEventRepository.findByEventIdAndUsername(id, username).stream()
                .map(UserEvent::getRole).map(Authority::getName).collect(Collectors.toSet());

        if(auths.contains("ADMIN") || auths.contains("ROLE_ADMIN")) {
            return taskRepository.findAllByEventId(id, pa).map(TaskDTO::new);
        }
        if(auths.contains("USER") || auths.contains("ROLE_USER")) {
            return taskRepository.findAllBy(id, username, pa).map(TaskDTO::new);
        }
        throw new IllegalStateException("unknows role");
    }

    @GetMapping("/events/{id}/messages")
    public Page<MessageDTO> getAllMessages(@PathVariable long id,Pageable pa) {
        return messageRepository
                .findAllByRoomIdOrderByCreatedDateAsc(id, pa)
                .map(msg -> new MessageDTO(msg, Boolean.TRUE));
    }

    /**
     * {@code DELETE  /events/:id} : delete the "id" eventDto.
     *
     * @param id the id of the eventDto to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/events/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        log.debug("REST request to delete Event : {}", id);

        List<NotificationEvent> list = notificationEventRepository.findByEventId(id);
        System.out.printf("=====> size"+list.size());
        Event event = eventRepository.findById(id).get();
        for(NotificationEvent e : list){
            e.setEvent(null);
            e.setDeleted(true);
            notificationEventRepository.save(e);
        }

        NotificationEvent notificationEvent = new NotificationEvent();
        User user = userService.getUserWithAuthorities().get();
        notificationEvent.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        notificationEvent.setReaded(false);
        notificationEvent.setDeleted(true);
        notificationEvent.setUser(user);
        notificationEvent.setType(6);
        notificationEvent.setContent("Xóa event: "+event.getName()+" thành công");
        notificationEvent.setEvent(null);
        notificationEventRepository.save(notificationEvent);

        for(Task t : taskRepository.findByEventListId(id)){
            for(NotificationTask n : notificationTaskRepository.findByTaskId(t.getId())){
                n.setTask(null);
                n.setDeleted(true);
                notificationTaskRepository.save(n);
            }
        }


        eventRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }


    @PostMapping("/addMemberToEvent")
    public UserEvent addMemberToEvent(@RequestParam("id") Long id, @RequestParam("iduser") Long idUser){
        User user = userRepository.findById(idUser).get();
        Event event = eventRepository.findById(id).get();
        UserEvent userEvent = new UserEvent();
        userEvent.setEvent(event);
        userEvent.setUser(user);
        User u = userRepository.findOneByLogin(event.getCreatedBy()).get();
        Authority authority = authorityRepository.findByName("ROLE_USER").get();
        userEvent.setRole(authority);
        userEvent.setCreatedBy(event.getCreatedBy());
        userEventRepository.save(userEvent);
        Notice notice = new Notice();
        notice.setContent("Bạn được thêm vào sự kiện " + event.getName());
        notice.getRegistrationTokens().add(user.getNotificationKey());
        Map<String,String> data = new HashMap<>();
        data.put("id", String.valueOf(event.getId()));
        data.put("type", "1");
        data.put("createdAt", DateTime.getDefaultInstance().toString());
        notice.setData(data);
        notificationService.sendNotification(notice);
        return userEvent;
    }

    @PostMapping("/deleteMember")
    public void deleteMember(@RequestParam("id") Long id, @RequestParam("username") String username) throws Exception {
        User currunUser =userService.getUserWithAuthorities().get();
        User user = userRepository.findOneByLogin(username).get();
        Event event = eventRepository.findById(id).get();
        if(!event.getCreatedBy().equals(currunUser.getLogin())){
            throw  new Exception("ban khong phai nguoi tao su kien");
        }

        UserEvent userEvent = userEventRepository.findByEventIdAndUsername(id, username).get(0);
        userEventRepository.delete(userEvent);
        Notice notice = new Notice();
        notice.setContent("Bạn bị xóa ra khỏi sự kiện " + event.getName());
        notice.getRegistrationTokens().add(user.getNotificationKey());
        Map<String,String> data = new HashMap<>();
        data.put("id", String.valueOf(event.getId()));
        data.put("type", "2");
        data.put("createdAt", DateTime.getDefaultInstance().toString());
        notice.setData(data);
        notificationService.sendNotification(notice);
    }

    @GetMapping("/notificationEvent/findByUser")
    public Page<NotificationEvent> findByEvent(Pageable pageable){
        return notificationEventRepository.findByUserId(userService.getUserWithAuthorities().get().getId(), pageable);
    }

    @GetMapping("/notificationEvent/readAll")
    public void updateReadNotification(){
        User user = userService.getUserWithAuthorities().get();
        for(NotificationEvent n : notificationEventRepository.findListByUserId(user.getId())){
            n.setReaded(true);
            notificationEventRepository.save(n);
        }
    }

    @GetMapping("/notificationEvent/notRead")
    public Long getNotificationNotRead(){
        User user = userService.getUserWithAuthorities().get();
        return notificationEventRepository.countNotificationNotRead(user.getId(), false);
    }
}
