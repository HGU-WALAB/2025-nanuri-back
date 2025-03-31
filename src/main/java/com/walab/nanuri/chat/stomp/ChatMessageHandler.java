package com.walab.nanuri.chat.stomp;

import com.walab.nanuri.chat.dto.request.ChatMessageRequestDto;
import com.walab.nanuri.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatMessageHandler {

    private final ChatMessageService chatMessageService;

    @MessageMapping("/chat/message")
    public void handleMessage(ChatMessageRequestDto dto) {
        chatMessageService.saveAndSend(dto);
    }
}