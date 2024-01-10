package com.mintyn.cardservice.repository;

import com.mintyn.cardservice.entity.CardInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardInfoRepository extends JpaRepository<CardInfo, Long> {
    CardInfo findByCardNumber(String cardNumber);
}
