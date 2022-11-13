package io.github.ntask_api.repository;

import io.github.ntask_api.domain.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Message entity.
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    Page<Message> findAllByRoomIdOrderByCreatedDateAsc(long id, Pageable pa);
}
