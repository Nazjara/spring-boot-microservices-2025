package com.nazjara.service;

import com.nazjara.dto.AccountsDto;
import com.nazjara.dto.CustomerDetailsDto;
import com.nazjara.exception.ResourceNotFoundException;
import com.nazjara.mapper.AccountsMapper;
import com.nazjara.mapper.CustomerMapper;
import com.nazjara.repository.AccountsRepository;
import com.nazjara.repository.CustomerRepository;
import com.nazjara.service.client.CardsFeignClient;
import com.nazjara.service.client.LoansFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ICustomerServiceImpl implements ICustomerService {

  private final AccountsRepository accountsRepository;
  private final CustomerRepository customerRepository;
  private final CardsFeignClient cardsFeignClient;
  private final LoansFeignClient loansFeignClient;

  @Override
  public CustomerDetailsDto getCustomerDetails(String mobileNumber) {
    var customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
        () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
    );

    var accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
        () -> new ResourceNotFoundException("Account", "customerId",
            customer.getCustomerId().toString())
    );

    var customerDto = CustomerMapper.mapToCustomerDetailsDto(customer, new CustomerDetailsDto());
    customerDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));
    customerDto.setLoansDto(loansFeignClient.fetchLoanDetails(mobileNumber).getBody());
    customerDto.setCardsDto(cardsFeignClient.fetchCardDetails(mobileNumber).getBody());

    return customerDto;
  }
}
