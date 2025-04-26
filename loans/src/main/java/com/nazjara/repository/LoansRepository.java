package com.nazjara.repository;

import com.nazjara.entity.Loans;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoansRepository extends JpaRepository<Loans, Long> {

  Optional<Loans> findByMobileNumber(String mobileNumber);

  Optional<Loans> findByLoanNumber(String loanNumber);
}
