package com.nazjara.controller;

import com.nazjara.dto.CustomerDetailsDto;
import com.nazjara.service.ICustomerService;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Validated
@RequiredArgsConstructor
public class CustomerController {

  private final ICustomerService iCustomerService;

  @GetMapping("/customer")
  public ResponseEntity<CustomerDetailsDto> getCustomerDetails(
      @RequestParam @Pattern(regexp = "(^$|[0-9]{10})",
          message = "Mobile number must be 10 digits") String mobileNumber) {
    return ResponseEntity.ok(iCustomerService.getCustomerDetails(mobileNumber));
  }
}
