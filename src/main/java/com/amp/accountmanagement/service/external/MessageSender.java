package com.amp.accountmanagement.service.external;

import com.amp.accountmanagement.model.dto.message.Message;

public interface MessageSender {

  void sendMessage(Message message);
}
