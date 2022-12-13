package io.github.ntask_api.repository;

import io.github.ntask_api.domain.NotificationEvent;
import io.github.ntask_api.domain.NotificationTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationEventRepository extends JpaRepository<NotificationEvent, Long> {

    @Query("select n from NotificationEvent n where n.event.id = ?1")
    public List<NotificationEvent> findByEventId(Long eventId);

    @Query("select n from NotificationEvent  n where n.user.id = ?1 and n.deleted_noti = false ")//and deleted = 0
    public Page<NotificationEvent> findByUserId(Long userId, Pageable pageable);

    @Query("select n from NotificationEvent  n where n.user.id = ?1 and n.deleted_noti = false ")
    public List<NotificationEvent> findListByUserId(Long userId);

    @Query("select count(n.id) from NotificationEvent  n where n.user.id = ?1 and n.readed = false ")
    public Long countNotificationNotRead(Long userId);
}
