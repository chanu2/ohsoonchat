package chatversion3.ohsoonchat.config;


import chatversion3.ohsoonchat.dto.ChatMessageSaveDto;
import chatversion3.ohsoonchat.exception.UserNotFoundException;
import chatversion3.ohsoonchat.model.Member;
import chatversion3.ohsoonchat.model.Reservation;
import chatversion3.ohsoonchat.property.JwtProperties;
import chatversion3.ohsoonchat.pubsub.RedisPublisher;
import chatversion3.ohsoonchat.repo.ChatRoomRepository;
import chatversion3.ohsoonchat.repo.ParticipationRepository;
import chatversion3.ohsoonchat.repo.ReservationRepository;
import chatversion3.ohsoonchat.repo.UserRepository;
import chatversion3.ohsoonchat.security.JwtTokenProvider;
import chatversion3.ohsoonchat.service.ChatRoomService;
import chatversion3.ohsoonchat.util.ChatUtils;
import chatversion3.ohsoonchat.util.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class StompHandler implements ChannelInterceptor {

    //private final JwtDecoder jwtDecoder;
    public static final String TOKEN = "token";
    public static final String SIMP_DESTINATION = "simpDestination";
    public static final String SIMP_SESSION_ID = "simpSessionId";
    public static final String INVALID_ROOM_ID = "InvalidRoomId";

    //private final HeaderTokenExtractor headerTokenExtractor;

    private final ChatUtils chatUtils;

    private final ChannelTopic topic;

    private final ChatRoomService chatRoomService;

    private final RedisPublisher redisPublisher;

    private final ParticipationRepository participationRepository;

    private final UserRepository userRepository;

    private final ReservationRepository reservationRepository;

    private final ChatRoomRepository chatRoomRepository;

    private final JwtTokenProvider jwtTokenProvider;

    private final JwtProperties jwtProperties;



    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT == accessor.getCommand()) {
//
//            String rawHeader = accessor.getFirstNativeHeader(jwtProperties.getHeader());
//
//            String token = jwtTokenProvider.resolveTokenWeb(rawHeader);
//
//            log.info("token={}",token);
//
//            jwtTokenProvider.validateToken(jwtTokenProvider.resolveTokenWeb(rawHeader));


            // TODO: 2023/08/14 웹소켓 테스트

            String rawHeader = accessor.getFirstNativeHeader(jwtProperties.getHeader());

            String token = jwtTokenProvider.resolveTokenWeb(rawHeader);

            log.info("token={}",token);

            jwtTokenProvider.validateToken(jwtTokenProvider.resolveTokenWeb(rawHeader));
            String userId = jwtTokenProvider.getUserId(token);

            log.info("userId={}",userId);

            Member member = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> UserNotFoundException.EXCEPTION);

            String roomId = accessor.getFirstNativeHeader(SIMP_DESTINATION);
//            String destination = Optional.ofNullable(
//                    (String) message.getHeaders().get(SIMP_DESTINATION)
//            ).orElse(INVALID_ROOM_ID);

            log.info("roomId={}",roomId);

            String sessionId = Optional.ofNullable(
                    (String) message.getHeaders().get(SIMP_SESSION_ID)
            ).orElse(null);

            log.info("sessionId={}",sessionId);

            //String roomId = chatUtils.getRoodIdFromDestination(destination);



            Reservation reservation = reservationRepository.findById(Long.valueOf(roomId)).orElseThrow(() -> UserNotFoundException.EXCEPTION);

            if(!participationRepository.existsByReservationAndMember(reservation,member)) {
                throw new RuntimeException("참여하지 않은 사람");
            }

            chatRoomService.enterChatRoom(roomId, sessionId, member.getName());

            redisPublisher.publish(topic,
                    ChatMessageSaveDto.builder()
                            .type(ChatMessageSaveDto.MessageType.ENTER)
                            .roomId(roomId)
                            .userList(chatRoomService.findUser(roomId, sessionId))
                            .build()
            );


        }


        // 소켓 연결 후 ,SUBSCRIBE 등록
        else if (StompCommand.SUBSCRIBE == accessor.getCommand()) {

            String rawHeader = accessor.getFirstNativeHeader(jwtProperties.getHeader());

            String token = jwtTokenProvider.resolveTokenWeb(rawHeader);

            log.info("token={}",token);

            jwtTokenProvider.validateToken(jwtTokenProvider.resolveTokenWeb(rawHeader));
            String userId = jwtTokenProvider.getUserId(token);

            log.info("userId={}",userId);

            Member member = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> UserNotFoundException.EXCEPTION);

            String destination = Optional.ofNullable(
                    (String) message.getHeaders().get(SIMP_DESTINATION)
            ).orElse(INVALID_ROOM_ID);

            String sessionId = Optional.ofNullable(
                    (String) message.getHeaders().get(SIMP_SESSION_ID)
            ).orElse(null);

            String roomId = chatUtils.getRoodIdFromDestination(destination);

            Reservation reservation = reservationRepository.findById(Long.valueOf(roomId)).orElseThrow(() -> UserNotFoundException.EXCEPTION);

            if(!participationRepository.existsByReservationAndMember(reservation,member)) {
                throw new RuntimeException("참여하지 않은 사람");
            }

            chatRoomService.enterChatRoom(roomId, sessionId, member.getName());

            redisPublisher.publish(topic,
                    ChatMessageSaveDto.builder()
                            .type(ChatMessageSaveDto.MessageType.ENTER)
                            .roomId(roomId)
                            .userList(chatRoomService.findUser(roomId, sessionId))
                            .build()
            );


        }

        //reids SubScribe 해제
        else if (StompCommand.UNSUBSCRIBE == accessor.getCommand()) {

            String sessionId = Optional.ofNullable(
                    (String) message.getHeaders().get(SIMP_SESSION_ID)
            ).orElse(null);

            String roomId = chatRoomService.leaveChatRoom(sessionId);

            redisPublisher.publish(topic,
                    ChatMessageSaveDto.builder()
                            .type(ChatMessageSaveDto.MessageType.QUIT)
                            .roomId(roomId)
                            .userList(chatRoomService.findUser(roomId, sessionId))
                            .build()
            );
        }

        //소켓 연결 후 , 소켓 연결 해제 시
        else if (StompCommand.DISCONNECT == accessor.getCommand()) {

            String sessionId = Optional.ofNullable(
                    (String) message.getHeaders().get(SIMP_SESSION_ID)
            ).orElse(null);

            log.info("sessionId={}",sessionId);

            String roomId = chatRoomService.disconnectWebsocket(sessionId);

            redisPublisher.publish(topic,
                    ChatMessageSaveDto.builder()
                            .type(ChatMessageSaveDto.MessageType.QUIT)
                            .roomId(roomId)
                            .userList(chatRoomService.findUser(roomId, sessionId))
                            .build()
            );

        }
        return message;
    }
}