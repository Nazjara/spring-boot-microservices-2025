package com.nazjara.service;

import com.nazjara.dto.CustomerDto;

/**
 * Interface for Account related operations
 */
public interface IAccountsService {

    /**
     * Creates a new account with the given customer details
     *
     * @param customerDto - CustomerDto Object containing customer and account details
     */
    void createAccount(CustomerDto customerDto);

    /**
     * Fetches account details based on a given mobile number
     *
     * @param mobileNumber - Input Mobile Number used to identify the customer
     * @return CustomerDto containing customer and account details
     */
    CustomerDto fetchAccount(String mobileNumber);

    /**
     * Updates account details for an existing customer
     *
     * @param customerDto - CustomerDto Object containing updated customer and account details
     * @return boolean indicating if the update of Account details is successful or not
     */
    boolean updateAccount(CustomerDto customerDto);

    /**
     * Deletes account details for a customer identified by mobile number
     *
     * @param mobileNumber - Input Mobile Number used to identify the customer whose account should be deleted
     * @return boolean indicating if the delete of Account details is successful or not
     */
    boolean deleteAccount(String mobileNumber);
}
