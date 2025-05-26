package com.nazjara.service.client;

import com.nazjara.dto.CardsDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class CardsFeignClientFallback implements CardsFeignClient {

  @Override
  public ResponseEntity<CardsDto> fetchCardDetails(String mobileNumber, String correlationId) {
    return ResponseEntity.ok(new CardsDto());
  }
}
