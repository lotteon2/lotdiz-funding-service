package com.lotdiz.fundingservice.messagequeue.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lotdiz.fundingservice.dto.request.CreateDeliveryRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryProducer {
  private final KafkaTemplate<String, String> kafkaTemplate;
  private final ObjectMapper objectMapper;

  public void sendCreateDelivery(CreateDeliveryRequestDto createDeliveryRequestDto) {
    try {
      String jsonString = objectMapper.writeValueAsString(createDeliveryRequestDto);
      kafkaTemplate.send("create-order", jsonString);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
