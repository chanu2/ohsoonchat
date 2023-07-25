package chatversion3.ohsoonchat.model;


import chatversion3.ohsoonchat.dto.ChatMessageSaveDto;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

import static jakarta.persistence.FetchType.LAZY;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
@Data
@Table(name = "chats")
public class Chat implements Serializable {

    private static final long serialVersionUID = 5090380600159441769L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "chat_id")
    private Long id;

    @Column
    private String message;

    @Column
    private String users;

    @Column
    private String createdAt;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    public static Chat of(ChatMessageSaveDto chatMessageSaveDto, Reservation reservation){

        return Chat.builder()
                .message(chatMessageSaveDto.getMessage())
                .createdAt(chatMessageSaveDto.getCreatedAt())
                .users(chatMessageSaveDto.getWriter())
                .reservation(reservation)
                .build();

    }
}