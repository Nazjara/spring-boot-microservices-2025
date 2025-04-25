package com.nazjara.service;

import com.nazjara.constant.AccountsConstants;
import com.nazjara.dto.AccountsDto;
import com.nazjara.dto.CustomerDto;
import com.nazjara.entity.Accounts;
import com.nazjara.entity.Customer;
import com.nazjara.exception.CustomerAlreadyExistsException;
import com.nazjara.exception.ResourceNotFoundException;
import com.nazjara.mapper.AccountsMapper;
import com.nazjara.mapper.CustomerMapper;
import com.nazjara.repository.AccountsRepository;
import com.nazjara.repository.CustomerRepository;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Implementation of the IAccountsService interface that provides
 * account-related operations
 */
@Service
@RequiredArgsConstructor
public class AccountsServiceImpl implements IAccountsService {

  private final AccountsRepository accountsRepository;
  private final CustomerRepository customerRepository;

  /**
   * Creates a new account with the given customer details
   *
   * @param customerDto - CustomerDto Object containing customer and account details
   * @throws CustomerAlreadyExistsException if a customer with the given mobile number already exists
   */
  @Override
  public void createAccount(CustomerDto customerDto) {
    var customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
    var optionalCustomer = customerRepository.findByMobileNumber(
        customerDto.getMobileNumber());

    if (optionalCustomer.isPresent()) {
      throw new CustomerAlreadyExistsException(
          "Customer already registered with given mobileNumber "
              + customerDto.getMobileNumber());
    }

    var savedCustomer = customerRepository.save(customer);
    accountsRepository.save(createNewAccount(savedCustomer));
  }

  /**
   * Creates a new account for a given customer with default values
   *
   * @param customer - Customer Object for whom the account is being created
   * @return the new account details
   */
  private Accounts createNewAccount(Customer customer) {
    var newAccount = new Accounts();
    newAccount.setCustomerId(customer.getCustomerId());
    var randomAccNumber = 1000000000L + new Random().nextInt(900000000);

    newAccount.setAccountNumber(randomAccNumber);
    newAccount.setAccountType(AccountsConstants.SAVINGS);
    newAccount.setBranchAddress(AccountsConstants.ADDRESS);
    return newAccount;
  }

  /**
   * Fetches account details based on a given mobile number
   *
   * @param mobileNumber - Input Mobile Number used to identify the customer
   * @return CustomerDto containing customer and account details
   * @throws ResourceNotFoundException if no customer or account is found with the given mobile number
   */
  @Override
  public CustomerDto fetchAccount(String mobileNumber) {
    var customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
        () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
    );

    var accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
        () -> new ResourceNotFoundException("Account", "customerId",
            customer.getCustomerId().toString())
    );

    var customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
    customerDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));
    return customerDto;
  }

  /**
   * Updates account details for an existing customer
   *
   * @param customerDto - CustomerDto Object containing updated customer and account details
   * @return boolean indicating if the update of Account details is successful or not
   * @throws ResourceNotFoundException if no account or customer is found with the given details
   */
  @Override
  public boolean updateAccount(CustomerDto customerDto) {
    var isUpdated = false;
    var accountsDto = customerDto.getAccountsDto();

    if (accountsDto != null) {
      var accounts = accountsRepository.findById(accountsDto.getAccountNumber()).orElseThrow(
          () -> new ResourceNotFoundException("Account", "AccountNumber",
              accountsDto.getAccountNumber().toString())
      );
      AccountsMapper.mapToAccounts(accountsDto, accounts);
      accounts = accountsRepository.save(accounts);

      var customerId = accounts.getCustomerId();
      var customer = customerRepository.findById(customerId).orElseThrow(
          () -> new ResourceNotFoundException("Customer", "CustomerID", customerId.toString())
      );
      CustomerMapper.mapToCustomer(customerDto, customer);
      customerRepository.save(customer);
      isUpdated = true;
    }

    return isUpdated;
  }

  /**
   * Deletes account details for a customer identified by mobile number
   *
   * @param mobileNumber - Input Mobile Number used to identify the customer whose account should be deleted
   * @return boolean indicating if the delete of Account details is successful or not
   * @throws ResourceNotFoundException if no customer is found with the given mobile number
   */
  @Override
  public boolean deleteAccount(String mobileNumber) {
    var customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
        () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
    );

    accountsRepository.deleteByCustomerId(customer.getCustomerId());
    customerRepository.deleteById(customer.getCustomerId());
    return true;
  }


}
