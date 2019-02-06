package com.amp.accountmanagement.service.external;

import com.amp.accountmanagement.model.dto.message.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AMPQMessageSender implements MessageSender {

  private static final String ROOT_EXCHANGE = "events-exchange";

  private RabbitTemplate rabbitTemplate;
  private ObjectMapper mapper;

  @Autowired
  public AMPQMessageSender(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
    this.mapper = new ObjectMapper();
    this.mapper.findAndRegisterModules();
    this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  @Override
  public void sendMessage(Message message) {
    try {
      rabbitTemplate.convertAndSend(
          ROOT_EXCHANGE, message.getPath(), mapper.writeValueAsString(message));
    } catch (JsonProcessingException e) {
      System.out.println(
          "Cannot send message for path: " + message.getPath() + ", JSON object is invalid");
    }
  }
}
