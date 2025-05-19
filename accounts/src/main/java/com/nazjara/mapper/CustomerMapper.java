package com.nazjara.mapper;

import com.nazjara.dto.CustomerDetailsDto;
import com.nazjara.dto.CustomerDto;
import com.nazjara.entity.Customer;
import lombok.experimental.UtilityClass;

// can use Mapstruct instead
@UtilityClass
public class CustomerMapper {

  public CustomerDto mapToCustomerDto(Customer customer, CustomerDto customerDto) {
    customerDto.setName(customer.getName());
    customerDto.setEmail(customer.getEmail());
    customerDto.setMobileNumber(customer.getMobileNumber());
    return customerDto;
  }

  public Customer mapToCustomer(CustomerDto customerDto, Customer customer) {
    customer.setName(customerDto.getName());
    customer.setEmail(customerDto.getEmail());
    customer.setMobileNumber(customerDto.getMobileNumber());
    return customer;
  }

  public CustomerDetailsDto mapToCustomerDetailsDto(Customer customer,
      CustomerDetailsDto customerDetailsDto) {
    customerDetailsDto.setName(customer.getName());
    customerDetailsDto.setEmail(customer.getEmail());
    customerDetailsDto.setMobileNumber(customer.getMobileNumber());
    return customerDetailsDto;
  }
}
