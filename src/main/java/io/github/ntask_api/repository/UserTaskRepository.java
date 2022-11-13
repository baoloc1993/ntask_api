package io.github.ntask_api.repository;

import io.github.ntask_api.domain.UserTask;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserTask entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserTaskRepository extends JpaRepository<UserTask, Long> {
    @Query("select userTask from UserTask userTask where userTask.user.login = ?#{principal.username}")
    List<UserTask> findByUserIsCurrentUser();
}
