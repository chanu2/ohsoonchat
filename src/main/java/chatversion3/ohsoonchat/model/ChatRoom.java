package chatversion3.ohsoonchat.model;//package chattest.project.model;
//
//import jakarta.persistence.*;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//import java.io.Serializable;
//
//import static jakarta.persistence.FetchType.LAZY;
//
//@Getter
//@NoArgsConstructor
//@Entity
//public class ChatRoom implements Serializable {
//
//    private static final long serialVersionUID = 6494678977089006639L;
//
//    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "chat_room_id")
//    private Long id;
//
//
//    @Column(nullable = false)
//    private String username;
//
//    @OneToOne(fetch = LAZY)
//    @JoinColumn(name = "reservation_id")
//    private Reservation reservation;
//
//
//    @Builder
//    public ChatRoom( String username,Reservation reservation) {
//        this.username = username;
//        this.reservation = reservation;
//    }
//
//    //채팅방 생성
//    public static ChatRoom create(Reservation reservation) {
//
//        return ChatRoom.builder()
//                .username(reservation.getMember().getName())
//                .build();
//
//    }
//}