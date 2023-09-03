package chatversion3.ohsoonchat.controller;

import chatversion3.ohsoonchat.model.Member;
import chatversion3.ohsoonchat.model.Participation;
import chatversion3.ohsoonchat.model.Reservation;
import chatversion3.ohsoonchat.repo.ChatRoomRepository;
import chatversion3.ohsoonchat.repo.ParticipationRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class Test {

    private final EntityManager em;
    private final ChatRoomRepository chatRoomRepository;
    private final ParticipationRepository participationRepository;

    @GetMapping("/test")
    public String test1(){
        return "hi my name is chanwoo ";
    }

    @PostMapping("/init")
    public void init(){

        Member member1 = Member.builder().email("mdsoo55828").name("김찬우").profilePath("absvdwd").build();
        Member member2 = Member.builder().email("awds@asdw").name("하재은").profilePath("absvdwd").build();
        Member member3 = Member.builder().email("@sndbwnd").name("김우탄").profilePath("absvdwd").build();

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);



        Reservation reservation = Reservation.builder().member(member1).title("해보자")
                .passengerNum(12).currentNum(0).build();

//            Reservation chatRoom = Reservation.builder().username("김찬우").reservation(reservation).build();
//            em.persist(chatRoom);

        em.persist(reservation);

        Participation participation = Participation.builder().member(member1).reservation(reservation).build();
        Participation participation2 = Participation.builder().member(member2).reservation(reservation).build();

        em.persist(participation);
        em.persist(participation2);

//        chatRoomRepository.enterChatRoom(String.valueOf(reservation.getId()),"asdwdanhbvd","김찬우");
//
//        String session = chatRoomRepository.getOpsHashEnterRoom().get("CHAT_ROOM_ID_1","asdwdanhbvd");

        em.flush();

        em.clear();

    }

}
