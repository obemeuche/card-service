package com.mintyn.cardservice.controller;

import com.mintyn.cardservice.response.CardStatsResponse;
import com.mintyn.cardservice.service.CardStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/card-scheme")
public class CardStatsController {

    private CardStatsService statsService;

    @GetMapping("/stats")
    public ResponseEntity<CardStatsResponse> getCardStats(
            @RequestParam(defaultValue = "1") int start,
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(statsService.getCardStats(start, limit));
    }
}
