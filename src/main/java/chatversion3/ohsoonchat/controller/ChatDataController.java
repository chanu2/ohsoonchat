package chatversion3.ohsoonchat.controller;


import chatversion3.ohsoonchat.dto.ChatPagingDto;
import chatversion3.ohsoonchat.dto.ChatPagingResponseDto;
import chatversion3.ohsoonchat.dto.ResponseDto;
import chatversion3.ohsoonchat.service.ChatRedisCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ChatDataController {

    private final ChatRedisCacheService cacheService;


    @PostMapping("/api/chats/{chatRoomId}")
    public ResponseDto<List<ChatPagingResponseDto>> getChatting(@PathVariable Long chatRoomId, @RequestBody(required = false) ChatPagingDto chatPagingDto){


        // TODO: 2023/07/19  시큐리티 컨텍스트에서 유저 정보를 들고온 후 방에 참여한 사람인지 확인  
        

        
        //Cursor 존재하지 않을 경우,현재시간을 기준으로 paging
        if(chatPagingDto==null||chatPagingDto.getCursor()==null || chatPagingDto.getCursor().equals("")){
            chatPagingDto= ChatPagingDto.builder()
                    .cursor( LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS")))
                    .build();
        }
        return cacheService.getChatsFromRedis(chatRoomId,chatPagingDto);
    }
}