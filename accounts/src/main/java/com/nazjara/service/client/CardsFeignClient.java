package com.nazjara.service.client;

import com.nazjara.dto.CardsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "cards")
public interface CardsFeignClient {

  @GetMapping("/api/fetch")
  ResponseEntity<CardsDto> fetchCardDetails(@RequestParam("mobileNumber") String mobileNumber,
      @RequestHeader("bank-correlation-id") String correlationId);
}
