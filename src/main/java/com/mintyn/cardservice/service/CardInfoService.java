package com.mintyn.cardservice.service;

import com.mintyn.cardservice.response.CardInfoResponse;

public interface CardInfoService {
    CardInfoResponse verifyCard(String cardNumber);
}
