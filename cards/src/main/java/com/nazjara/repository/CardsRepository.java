package com.nazjara.repository;

import com.nazjara.entity.Cards;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardsRepository extends JpaRepository<Cards, Long> {

  Optional<Cards> findByMobileNumber(String mobileNumber);

  Optional<Cards> findByCardNumber(String cardNumber);
}
