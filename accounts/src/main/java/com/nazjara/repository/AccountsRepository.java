package com.nazjara.repository;

import com.nazjara.entity.Accounts;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountsRepository extends JpaRepository<Accounts, Long> {

  Optional<Accounts> findByCustomerId(Long customerId);

  void deleteByCustomerId(Long customerId);
}
