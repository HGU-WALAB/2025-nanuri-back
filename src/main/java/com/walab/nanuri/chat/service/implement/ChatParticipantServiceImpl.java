package com.walab.nanuri.chat.service.implement;

import com.walab.nanuri.chat.entity.ChatRoom;

public interface ChatParticipantServiceImpl {
    void enterRoom(ChatRoom chatRoom, String userId);
    void leaveRoom(Long roomKey, String userId);
}
