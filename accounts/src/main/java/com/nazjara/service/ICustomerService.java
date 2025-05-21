package com.nazjara.service;

import com.nazjara.dto.CustomerDetailsDto;

public interface ICustomerService {

  CustomerDetailsDto getCustomerDetails(String mobileNumber, String correlationId);
}
