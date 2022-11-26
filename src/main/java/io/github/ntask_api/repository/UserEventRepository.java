package io.github.ntask_api.repository;

import io.github.ntask_api.domain.UserEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserEventRepository extends JpaRepository<UserEvent, Long> {
    @Query("select e from UserEvent e where e.user.login = ?#{principal.username}")
    List<UserEvent> findByUserIsCurrentUser();

    @Query("select e from UserEvent e where e.event.id = ?1 and e.user.login = ?2")
    List<UserEvent> findByEventIdAndUsername(Long id, String username);


    int deleteAllByEventId(Long id);
    List<UserEvent> findAllByEventId(Long id);
}
