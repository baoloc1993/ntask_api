package io.github.ntask_api.web.rest;

import io.github.ntask_api.domain.NotificationTask;
import io.github.ntask_api.domain.User;
import io.github.ntask_api.repository.NotificationTaskRepository;
import io.github.ntask_api.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

@RestController
@RequestMapping("/api")
public class NotificationTaskResouces {

    private final NotificationTaskRepository notificationTaskRepository;

    private final UserService userService;

    public NotificationTaskResouces(NotificationTaskRepository notificationTaskRepository, UserService userService) {
        this.notificationTaskRepository = notificationTaskRepository;
        this.userService = userService;
    }

    @PostMapping("/notificationTask")
    public NotificationTask save(@RequestBody NotificationTask notificationTask){
        User user = userService.getUserWithAuthorities().get();
        notificationTask.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        notificationTask.setReaded(false);
        notificationTask.setDeleted(false);
        notificationTask.setUser(user);
        NotificationTask result =  notificationTaskRepository.save(notificationTask);
        return result;
    }

    @PostMapping("/notificationTask/read")
    public NotificationTask read(@RequestParam("id")  Long id){
        NotificationTask notificationTask = notificationTaskRepository.findById(id).get();
        notificationTask.setReaded(true);
        notificationTaskRepository.save(notificationTask);
        return notificationTask;
    }

    @PostMapping("/notificationTask/delete")
    public NotificationTask delete(@RequestParam("id")  Long id){
        NotificationTask notificationTask = notificationTaskRepository.findById(id).get();
        notificationTask.setDeleted_noti(true);
        notificationTask.setTask(null);
        notificationTaskRepository.save(notificationTask);
        return notificationTask;
    }
}
