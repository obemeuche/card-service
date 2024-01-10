package com.mintyn.cardservice.controller;

import com.mintyn.cardservice.response.CardInfoResponse;
import com.mintyn.cardservice.service.CardInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/card-scheme")
public class CardInfoController {

    private CardInfoService cardService;


    @GetMapping("/verify/{cardNumber}")
    public ResponseEntity<CardInfoResponse> verifyCard(@PathVariable String cardNumber) {
        return ResponseEntity.ok(cardService.verifyCard(cardNumber));
    }
}
