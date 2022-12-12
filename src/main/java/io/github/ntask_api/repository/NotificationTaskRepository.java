package io.github.ntask_api.repository;

import io.github.ntask_api.domain.Message;
import io.github.ntask_api.domain.NotificationTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationTaskRepository  extends JpaRepository<NotificationTask, Long> {

    @Query("select n from NotificationTask  n where n.task.id = ?1")
    public List<NotificationTask> findByTaskId(Long taskId);

    @Query("select n from NotificationTask  n where n.user.id = ?1 and n.deleted_noti = false ")
    public Page<NotificationTask> findByUserId(Long userId, Pageable pageable);

    @Query("select n from NotificationTask  n where n.user.id = ?1")
    public List<NotificationTask> findListByUserId(Long userId);

    @Query("select count(n.id) from NotificationTask  n where n.user.id = ?1 and n.readed = false ")
    public Long countNotificationNotRead(Long userId);
}
