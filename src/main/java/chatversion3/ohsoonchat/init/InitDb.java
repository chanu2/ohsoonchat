//package chatversion3.ohsoonchat.init;
//
//
//
//
//import chatversion3.ohsoonchat.model.Member;
//import chatversion3.ohsoonchat.model.Participation;
//import chatversion3.ohsoonchat.model.Reservation;
//import chatversion3.ohsoonchat.repo.ChatRoomRepository;
//import chatversion3.ohsoonchat.repo.ParticipationRepository;
//import jakarta.annotation.PostConstruct;
//import jakarta.persistence.EntityManager;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//@Component  // 스프링빈 등록
//@RequiredArgsConstructor
//public class InitDb {
//
//
//
//    private final InitService initService;
//
//    @PostConstruct  //bean이 여러 번 초기화되는 걸 방지할 수 있다.
//    public void init() {
//        initService.dbInit1();
//    }
//
//    @Component
//    @Transactional
//    @RequiredArgsConstructor // 생성자 주입
//    @Slf4j
//    static class InitService {
//
//        private final EntityManager em;
//        private final ChatRoomRepository chatRoomRepository;
//        private final ParticipationRepository participationRepository;
//
//
//        public void dbInit1() {
//
//            Member member1 = Member.builder().email("mdsoo55828").name("김찬우").profilePath("absvdwd").build();
//            Member member2 = Member.builder().email("awds@asdw").name("하재은").profilePath("absvdwd").build();
//            Member member3 = Member.builder().email("@sndbwnd").name("김우탄").profilePath("absvdwd").build();
//
//            em.persist(member1);
//            em.persist(member2);
//            em.persist(member3);
//
//
//
//            Reservation reservation = Reservation.builder().member(member1).title("해보자")
//                    .passengerNum(12).currentNum(0).build();
//
////            Reservation chatRoom = Reservation.builder().username("김찬우").reservation(reservation).build();
////            em.persist(chatRoom);
//
//            em.persist(reservation);
//
//            Participation participation = Participation.builder().member(member1).reservation(reservation).build();
//            Participation participation2 = Participation.builder().member(member2).reservation(reservation).build();
//
//            em.persist(participation);
//            em.persist(participation2);
//
//            chatRoomRepository.enterChatRoom(String.valueOf(reservation.getId()),"asdwdanhbvd","김찬우");
//
//            String session = chatRoomRepository.getOpsHashEnterRoom().get("CHAT_ROOM_ID_1","asdwdanhbvd");
//
//
//            log.info("session={}",session);
//
//            em.flush();
//
//            em.clear();
//
//
//        }
//
//
//
//
//    }
//}
