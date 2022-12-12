package io.github.ntask_api.web.rest;

import io.github.ntask_api.domain.NotificationEvent;
import io.github.ntask_api.domain.User;
import io.github.ntask_api.repository.NotificationEventRepository;
import io.github.ntask_api.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

@RestController
@RequestMapping("/api")
public class NotificationEventResouces {

    private final NotificationEventRepository notificationEventRepository;

    private final UserService userService;

    public NotificationEventResouces(NotificationEventRepository notificationEventRepository, UserService userService) {
        this.notificationEventRepository = notificationEventRepository;
        this.userService = userService;
    }

    @PostMapping("/notificationEvent")
    public NotificationEvent save(@RequestBody NotificationEvent notificationEvent){
        User user = userService.getUserWithAuthorities().get();
        notificationEvent.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        notificationEvent.setReaded(false);
        notificationEvent.setDeleted(false);
        notificationEvent.setUser(user);
        NotificationEvent result =  notificationEventRepository.save(notificationEvent);
        return result;
    }

    @PostMapping("/notificationEvent/read")
    public NotificationEvent read(@RequestParam("id")  Long id){
        NotificationEvent notificationTask = notificationEventRepository.findById(id).get();
        notificationTask.setReaded(true);
        notificationEventRepository.save(notificationTask);
        return notificationTask;
    }

    @PostMapping("/notificationEvent/delete")
    public NotificationEvent delete(@RequestParam("id")  Long id){
        NotificationEvent notificationTask = notificationEventRepository.findById(id).get();
        notificationTask.setDeleted_noti(true);
        notificationTask.setEvent(null);
        notificationEventRepository.save(notificationTask);
        return notificationTask;
    }
}
