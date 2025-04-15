package com.walab.nanuri.chat.stomp;

import com.walab.nanuri.chat.dto.request.ChatMessageRequestDto;
import com.walab.nanuri.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatMessageHandler {

    private final ChatMessageService chatMessageService;

    @MessageMapping("/chat/message")
    public void handleMessage(@AuthenticationPrincipal String uniqueId, ChatMessageRequestDto dto) {
        chatMessageService.saveAndSend(uniqueId, dto);
    }
}