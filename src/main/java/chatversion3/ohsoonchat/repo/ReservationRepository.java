package chatversion3.ohsoonchat.repo;




import chatversion3.ohsoonchat.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long>{


    List<Reservation> findTop5ByOrderByIdDesc();

    @Query("select distinct r from Reservation r"+
            " join fetch r.participations p"+
            " where p.member.id = :userId")
    List<Reservation> findParticipatedReservation(@Param("userId") Long userId);
}
