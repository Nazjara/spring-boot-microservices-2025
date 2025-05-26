package com.nazjara.service.client;

import com.nazjara.dto.LoansDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "loans", fallback = LoansFeignClientFallback.class)
public interface LoansFeignClient {

  @GetMapping("/api/fetch")
  ResponseEntity<LoansDto> fetchLoanDetails(@RequestParam("mobileNumber") String mobileNumber,
      @RequestHeader("bank-correlation-id") String correlationId);
}
