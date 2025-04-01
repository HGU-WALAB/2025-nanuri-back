package com.walab.nanuri.chat.controller;

import com.walab.nanuri.chat.dto.request.ChatRoomRequestDto;
import com.walab.nanuri.chat.dto.response.ChatMessageResponseDto;
import com.walab.nanuri.chat.dto.response.ChatRoomResponseDto;
import com.walab.nanuri.chat.service.ChatMessageService;
import com.walab.nanuri.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/chat")
public class ChatController {
    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;

    @GetMapping("/rooms")
    public ResponseEntity<List<ChatRoomResponseDto>> getRooms(@AuthenticationPrincipal String uniqueId){
        return ResponseEntity.ok().body(chatRoomService.getChatRooms(uniqueId));
    }

    @GetMapping("/room/{roomId}/messages")
    public ResponseEntity<List<ChatMessageResponseDto>> getMessagesInRoom(
                                                            @AuthenticationPrincipal String uniqueId,
                                                            @PathVariable Long roomId,
                                                            @RequestBody ChatRoomRequestDto.ChatRoomUserValidationRequest request,
                                                            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime cursor
    ){
        return ResponseEntity.ok().body(chatMessageService.getChatMessages(uniqueId , roomId ,request, cursor));
    }

    @DeleteMapping("/room/{roomId}")
    public ResponseEntity<?> deleteRoom(@AuthenticationPrincipal String uniqueId, @PathVariable Long roomId) {
        chatRoomService.deleteChatRoom(uniqueId, roomId);
        return ResponseEntity.ok().build();
    }
}
