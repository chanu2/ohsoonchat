package chatversion3.ohsoonchat.dto;


import chatversion3.ohsoonchat.model.Chat;
import lombok.*;

@Getter
@NoArgsConstructor
@Builder
@Setter
@AllArgsConstructor
public class ChatPagingResponseDto {

    private Long reservationId;
    private String writer;
    private String message;
    private String createdAt;
    private String nickname;

    public static ChatPagingResponseDto of(Chat chat){
        return ChatPagingResponseDto.builder()
                .writer(chat.getUsers())
                .reservationId(chat.getReservation().getId())
                .createdAt(chat.getCreatedAt())
                .message(chat.getMessage())
                .build();
    }

    public static ChatPagingResponseDto byChatMessageDto(ChatMessageSaveDto chatMessageSaveDto){
        return ChatPagingResponseDto.builder()
                .writer(chatMessageSaveDto.getWriter())
                .createdAt(chatMessageSaveDto.getCreatedAt())
                .reservationId(Long.parseLong(chatMessageSaveDto.getRoomId()))
                .message(chatMessageSaveDto.getMessage())
                .build();
    }
}