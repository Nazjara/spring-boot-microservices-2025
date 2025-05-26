package com.nazjara.service.client;

import com.nazjara.dto.LoansDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class LoansFeignClientFallback implements LoansFeignClient {

  @Override
  public ResponseEntity<LoansDto> fetchLoanDetails(String mobileNumber, String correlationId) {
    return ResponseEntity.ok(new LoansDto());
  }
}
