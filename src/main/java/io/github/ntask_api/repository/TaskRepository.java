package io.github.ntask_api.repository;

import io.github.ntask_api.domain.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Task entity.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findAllByEventId(Long id, Pageable pa);

    @Query("SELECT DISTINCT t FROM UserTask ut JOIN ut.task t JOIN ut.user u JOIN t.event e WHERE u.id = ?1 AND e.id = ?2")
    Page<Task> findAllBy(Long uid, Long eid, Pageable pageable);

    @Query("SELECT DISTINCT t FROM UserTask ut JOIN ut.task t join ut.user u JOIN t.event e WHERE e.id = ?1 AND u.login = ?2")
    Page<Task> findAllBy(Long eid, String username, Pageable pa);

}
