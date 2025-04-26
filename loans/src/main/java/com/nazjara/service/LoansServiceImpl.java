package com.nazjara.service;

import com.nazjara.constants.LoansConstants;
import com.nazjara.dto.LoansDto;
import com.nazjara.entity.Loans;
import com.nazjara.exception.LoanAlreadyExistsException;
import com.nazjara.exception.ResourceNotFoundException;
import com.nazjara.mapper.LoansMapper;
import com.nazjara.repository.LoansRepository;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoansServiceImpl implements ILoansService {

  private final LoansRepository loansRepository;

  /**
   * @param mobileNumber - Mobile Number of the Customer
   */
  @Override
  public void createLoan(String mobileNumber) {
    var optionalLoans = loansRepository.findByMobileNumber(mobileNumber);

    if (optionalLoans.isPresent()) {
      throw new LoanAlreadyExistsException(
          "Loan already registered with given mobileNumber " + mobileNumber);
    }

    loansRepository.save(createNewLoan(mobileNumber));
  }

  /**
   * @param mobileNumber - Mobile Number of the Customer
   * @return the new loan details
   */
  private Loans createNewLoan(String mobileNumber) {
    var newLoan = new Loans();
    long randomLoanNumber = 100000000000L + new Random().nextInt(900000000);
    newLoan.setLoanNumber(Long.toString(randomLoanNumber));
    newLoan.setMobileNumber(mobileNumber);
    newLoan.setLoanType(LoansConstants.HOME_LOAN);
    newLoan.setTotalLoan(LoansConstants.NEW_LOAN_LIMIT);
    newLoan.setAmountPaid(0);
    newLoan.setOutstandingAmount(LoansConstants.NEW_LOAN_LIMIT);
    return newLoan;
  }

  /**
   * @param mobileNumber - Input mobile Number
   * @return Loan Details based on a given mobileNumber
   */
  @Override
  public LoansDto fetchLoan(String mobileNumber) {
    var loans = loansRepository.findByMobileNumber(mobileNumber).orElseThrow(
        () -> new ResourceNotFoundException("Loan", "mobileNumber", mobileNumber)
    );

    return LoansMapper.mapToLoansDto(loans, new LoansDto());
  }

  /**
   * @param loansDto - LoansDto Object
   * @return boolean indicating if the update of loan details is successful or not
   */
  @Override
  public boolean updateLoan(LoansDto loansDto) {
    var loans = loansRepository.findByLoanNumber(loansDto.getLoanNumber()).orElseThrow(
        () -> new ResourceNotFoundException("Loan", "LoanNumber", loansDto.getLoanNumber()));

    LoansMapper.mapToLoans(loansDto, loans);
    loansRepository.save(loans);
    return true;
  }

  /**
   * @param mobileNumber - Input MobileNumber
   * @return boolean indicating if the delete of loan details is successful or not
   */
  @Override
  public boolean deleteLoan(String mobileNumber) {
    var loans = loansRepository.findByMobileNumber(mobileNumber).orElseThrow(
        () -> new ResourceNotFoundException("Loan", "mobileNumber", mobileNumber)
    );

    loansRepository.deleteById(loans.getLoanId());
    return true;
  }
}
