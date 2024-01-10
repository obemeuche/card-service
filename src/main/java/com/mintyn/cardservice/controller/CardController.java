package com.mintyn.cardservice.controller;

import com.mintyn.cardservice.response.CardInfoResponse;
import com.mintyn.cardservice.response.CardStatsResponse;
import com.mintyn.cardservice.service.CardInfoService;
import com.mintyn.cardservice.service.CardStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/card-scheme")
public class CardController {

    private CardInfoService cardService;

    private CardStatsService statsService;

    @GetMapping("/verify/{cardNumber}")
    public ResponseEntity<CardInfoResponse> verifyCard(@PathVariable String cardNumber) {
        return ResponseEntity.ok(cardService.verifyCard(cardNumber));
    }

    @GetMapping("/stats")
    public ResponseEntity<CardStatsResponse> getCardStats(
            @RequestParam(defaultValue = "1") int start,
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(statsService.getCardStats(start, limit));
    }
}
