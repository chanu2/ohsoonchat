package chatversion3.ohsoonchat.service;



import chatversion3.ohsoonchat.model.Member;
import chatversion3.ohsoonchat.model.Participation;
import chatversion3.ohsoonchat.model.Reservation;
import chatversion3.ohsoonchat.repo.ParticipationRepository;
import chatversion3.ohsoonchat.repo.ReservationRepository;
import chatversion3.ohsoonchat.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ParticipationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final ParticipationRepository participationRepository;


    public Participation checkRoom(Long roomId, Long userId){

        Optional<Reservation> reservation = reservationRepository.findById(roomId);
        Optional<Member> user = userRepository.findById(userId);


        return participationRepository.findByReservationAndMember(reservation.get(),user.get());

    }
}
