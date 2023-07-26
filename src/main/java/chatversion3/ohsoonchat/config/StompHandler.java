package chatversion3.ohsoonchat.config;


import chatversion3.ohsoonchat.dto.ChatMessageSaveDto;
import chatversion3.ohsoonchat.exception.UserNotFoundException;
import chatversion3.ohsoonchat.model.Member;
import chatversion3.ohsoonchat.model.Reservation;
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



    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // 최초 소켓 연결
//        if (StompCommand.CONNECT == accessor.getCommand()) {
//
//            //String headerToken = accessor.getFirstNativeHeader(TOKEN);
//
//            Long uid = Long.valueOf(accessor.getFirstNativeHeader("uid"));
//            log.info("----------------------");
//
//            log.info((String) message.getHeaders().get("uid"));
//            //Long uid = (Long) message.getHeaders().get("uid");
//            log.info("uid={}",uid);
//
//            //String roomId = (String) message.getHeaders().get("roomId");
//            log.info((String) message.getHeaders().get("roomId"));
//            String roomId = accessor.getFirstNativeHeader("roomId");
//            log.info("roomId={}",roomId);
//
//            log.info((String) message.getHeaders().get("sessionId"));
//            //String sessionId = (String) message.getHeaders().get("sessionId");
//            String sessionId = accessor.getFirstNativeHeader("sessionId");
//            log.info("sessionId={}",sessionId);
//
//            log.error("1");
//
//            Member member = userRepository.findById(uid).orElseThrow(() -> UserNotFoundException.EXCEPTION);
//            Reservation reservation = reservationRepository.findById(Long.valueOf(roomId)).orElseThrow(() -> UserNotFoundException.EXCEPTION);
//
//            if(!participationRepository.existsByReservationAndMember(reservation,member)) {
//                throw new RuntimeException("참여하지 않은 사람");
//            }
//
//            chatRoomService.enterChatRoom(roomId, sessionId, member.getName());
//
//            // test
//            Map<String, String> chatRoomId1 = chatRoomRepository.getOpsHashEnterRoom().entries("CHAT_ROOM_ID_1");
//            log.info("leng={}",chatRoomId1.size());
//
//            for (String value : chatRoomId1.values()) {
//                log.info("value={}",value);
//            }
//
//
//
//            // TODO: 2023/07/18 인증 관련 로직 추가
//            //String token = headerTokenExtractor.extract(headerToken);
//            //log.info(jwtDecoder.decodeUsername(token).getUsername());
//
//        }



        if (StompCommand.CONNECT == accessor.getCommand()) {

            String token = jwtTokenProvider.resolveTokenWeb(accessor);

            log.info("token={}",token);

            // jwtTokenProvider.getAuthentication(token);
            //Long currentUserId = SecurityUtils.getCurrentUserId();

            jwtTokenProvider.getJws(token);
            String userId = jwtTokenProvider.getUserId(token);

            log.info("userId={}",userId);


            //String headerToken = accessor.getFirstNativeHeader(TOKEN);

            Long uid = Long.valueOf(accessor.getFirstNativeHeader("uid"));
            log.info("----------------------");

            log.info((String) message.getHeaders().get("uid"));
            //Long uid = (Long) message.getHeaders().get("uid");
            log.info("uid={}",uid);

            //String roomId = (String) message.getHeaders().get("roomId");
            log.info((String) message.getHeaders().get("roomId"));
            String roomId = accessor.getFirstNativeHeader("roomId");
            log.info("roomId={}",roomId);

            log.info((String) message.getHeaders().get("sessionId"));
            //String sessionId = (String) message.getHeaders().get("sessionId");
            String sessionId = accessor.getFirstNativeHeader("sessionId");
            log.info("sessionId={}",sessionId);

            log.error("1");

            Member member = userRepository.findById(uid).orElseThrow(() -> UserNotFoundException.EXCEPTION);
            Reservation reservation = reservationRepository.findById(Long.valueOf(roomId)).orElseThrow(() -> UserNotFoundException.EXCEPTION);

            if(!participationRepository.existsByReservationAndMember(reservation,member)) {
                throw new RuntimeException("참여하지 않은 사람");
            }

            chatRoomService.enterChatRoom(roomId, sessionId, member.getName());

            // test
            Map<String, String> chatRoomId1 = chatRoomRepository.getOpsHashEnterRoom().entries("CHAT_ROOM_ID_1");
            log.info("leng={}",chatRoomId1.size());

            for (String value : chatRoomId1.values()) {
                log.info("value={}",value);
            }



            // TODO: 2023/07/18 인증 관련 로직 추가
            //String token = headerTokenExtractor.extract(headerToken);
            //log.info(jwtDecoder.decodeUsername(token).getUsername());

        }






        // 소켓 연결 후 ,SUBSCRIBE 등록
        else if (StompCommand.SUBSCRIBE == accessor.getCommand()) {

            // TODO: 2023/07/18 로그 찍기 
            //log.info("SubScribe destination : " + message.getHeaders().get(SIMP_DESTINATION));
            //log.info("SubScribe sessionId : " + message.getHeaders().get(SIMP_SESSION_ID));

            // TODO: 2023/07/18 토큰을 인증해서 가져오고 커텍스트를 통해서 유저 가져오기
            //String headerToken = accessor.getFirstNativeHeader(TOKEN);
           // String token = headerTokenExtractor.extract(headerToken);
            //String username = jwtDecoder.decodeUsername(token).getUsername();

            // TODO: 2023/07/18 test를 위한 유저값 직접 가져오기 삭제 예정!
            String username = accessor.getFirstNativeHeader("USER");

            // TODO: 2023/07/18  destination 헤더에서 가져오기
//            String destination = Optional.ofNullable(
//                    (String) message.getHeaders().get(SIMP_DESTINATION)
//            ).orElse(INVALID_ROOM_ID);

            // TODO: 2023/07/18  sessionid 헤더에서 꺼내기
//            String sessionId = Optional.ofNullable(
//                    (String) message.getHeaders().get(SIMP_SESSION_ID)
//            ).orElse(null);

            String sessionId = (String) message.getHeaders().get("sessionId");

            // TODO: 2023/07/18 가져온 destination에서 roomid꺼내기
//            String roomId = chatUtils.getRoodIdFromDestination(destination);
            String roomId = (String) message.getHeaders().get("roomId");

            //redis에  key(roomId) :  Value( sessionId , nickname ) 저장

            chatRoomService.enterChatRoom(roomId, sessionId, username);


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