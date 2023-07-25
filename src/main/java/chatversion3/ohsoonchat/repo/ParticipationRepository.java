package chatversion3.ohsoonchat.repo;




import chatversion3.ohsoonchat.model.Member;
import chatversion3.ohsoonchat.model.Participation;
import chatversion3.ohsoonchat.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {
    boolean existsByReservationAndMember(Reservation reservation, Member member);
    boolean existsByMemberIdAndReservationId(Long memberId,Long reservationId);
    List<Participation> findAllByReservation(Reservation reservation);
    List<Participation> findAllByReservationId(Long reservationId);
    void deleteByMemberIdAndReservationId(Long memberId,Long reservationId);
    List<Participation> findAllByMember(Member member);
    Participation findByReservationAndMember(Reservation reservation ,Member member);
}
