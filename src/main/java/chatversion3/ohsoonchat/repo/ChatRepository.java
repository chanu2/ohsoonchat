package chatversion3.ohsoonchat.repo;


import chatversion3.ohsoonchat.model.Chat;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat,Long> {

    Slice<Chat> findAllByCreatedAtBeforeAndReservationIdOrderByCreatedAtDesc(String cursorCreatedAt, Long reservationId, Pageable pageable);

    List<Chat> findAllByCreatedAtAfterOrderByCreatedAtDesc(String cursorCreatedAt);
}