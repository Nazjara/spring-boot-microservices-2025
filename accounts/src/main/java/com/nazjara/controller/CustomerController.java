package com.nazjara.controller;

import com.nazjara.dto.CustomerDetailsDto;
import com.nazjara.service.ICustomerService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Validated
@RequiredArgsConstructor
@Slf4j
public class CustomerController {

  private final ICustomerService iCustomerService;

  @GetMapping("/customer")
  @RateLimiter(name = "customer", fallbackMethod = "getCustomerDetailsFallback")
  public ResponseEntity<CustomerDetailsDto> getCustomerDetails(
      @RequestHeader("bank-correlation-id") String correlationId,
      @RequestParam @Pattern(regexp = "(^$|[0-9]{10})",
          message = "Mobile number must be 10 digits") String mobileNumber) {
    log.info("getCustomerDetails request initiated");
    return ResponseEntity.ok(iCustomerService.getCustomerDetails(mobileNumber, correlationId));
  }

  public ResponseEntity<CustomerDetailsDto> getCustomerDetailsFallback(String correlationId,
      String mobileNumber, Throwable throwable) {
    log.error("Rate limit reached while fetching customer details: {}", throwable.getMessage());
    return ResponseEntity.ok(new CustomerDetailsDto());
  }
}
