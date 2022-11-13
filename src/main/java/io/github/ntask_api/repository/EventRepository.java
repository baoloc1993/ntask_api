package io.github.ntask_api.repository;

import io.github.ntask_api.domain.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Event entity.
 */
@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("SELECT DISTINCT e FROM UserEvent ue join ue.event e join ue.user u WHERE u.id = ?1")
    Page<Event> findAllBy(Long uid, Pageable pageable);

    @Query("SELECT DISTINCT e FROM UserEvent ue join ue.event e WHERE e.id in ?1")
    Page<Event> findAllByIDs(Iterable<Long> ids, Pageable pageable);

}
