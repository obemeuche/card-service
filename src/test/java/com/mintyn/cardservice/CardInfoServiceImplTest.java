package com.mintyn.cardservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mintyn.cardservice.apiCall.WebClientCall;
import com.mintyn.cardservice.entity.CardInfo;
import com.mintyn.cardservice.exceptions.ConnectionException;
import com.mintyn.cardservice.exceptions.DatabaseException;
import com.mintyn.cardservice.repository.CardInfoRepository;
import com.mintyn.cardservice.response.BinListResponse;
import com.mintyn.cardservice.response.CardInfoResponse;
import com.mintyn.cardservice.response.dto.Bank;
import com.mintyn.cardservice.service.serviceImpl.CardInfoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CardInfoServiceImplTest {

    @Mock
    private WebClientCall webClientCall;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private CardInfoRepository cardInfoRepository;

    @InjectMocks
    private CardInfoServiceImpl cardInfoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void verifyCard_WithExistingCardNumber_ShouldReturnValidCardInfoResponse() {
        // Arrange
        String cardNumber = "12345678";
        CardInfo cardInfo = new CardInfo();
        cardInfo.setCardNumber(cardNumber);
        cardInfo.setScheme("visa");
        cardInfo.setType("debit");
        cardInfo.setBank("TestBank");

        when(cardInfoRepository.findByCardNumber(cardNumber)).thenReturn(cardInfo);

        // Act
        CardInfoResponse result = cardInfoService.verifyCard(cardNumber);

        // Assert
        assertTrue(result.isSuccess());
        assertEquals("visa", result.getPayload().getScheme());
        assertEquals("debit", result.getPayload().getType());
        assertEquals("TestBank", result.getPayload().getBank());

        verify(cardInfoRepository, times(1)).findByCardNumber(cardNumber);
        verifyNoMoreInteractions(webClientCall, objectMapper, cardInfoRepository);
    }

    @Test
    void verifyCard_WithNonExistingCardNumber_ShouldReturnValidCardInfoResponse()
    {
        String cardNumber = "12345678";

        Bank bank = new Bank();
        bank.setName("TestBank");

        BinListResponse binListResponse = new BinListResponse();
        binListResponse.setScheme("mastercard");
        binListResponse.setType("credit");
        binListResponse.setBank(bank);

        when(cardInfoRepository.findByCardNumber(cardNumber)).thenReturn(null);
        when(webClientCall.cardServiceApiCall(cardNumber)).thenReturn((HashMap<String, Object>) Map.of(
                "number", Map.of("length", 16, "luhn", true),
                "scheme", "visa",
                "type", "debit",
                "brand", "Visa/Dankort",
                "prepaid", false,
                "country", Map.of(
                        "numeric", "208",
                        "alpha2", "DK",
                        "name", "Denmark",
                        "emoji", "🇩🇰",
                        "currency", "DKK",
                        "latitude", 56,
                        "longitude", 10
                ),
                "bank", Map.of(
                        "name", "Jyske Bank",
                        "url", "www.jyskebank.dk",
                        "phone", "+4589893300",
                        "city", "Hjørring"
                )
        ));
        when(objectMapper.convertValue(any(), eq(BinListResponse.class))).thenReturn(binListResponse);

        CardInfoResponse result = cardInfoService.verifyCard(cardNumber);

        assertTrue(result.isSuccess());
        assertEquals("mastercard", result.getPayload().getScheme());
        assertEquals("credit", result.getPayload().getType());
        assertEquals("TestBank", result.getPayload().getBank());

        verify(cardInfoRepository, times(1)).findByCardNumber(cardNumber);
        verify(webClientCall, times(1)).cardServiceApiCall(cardNumber);
        verify(objectMapper, times(1)).convertValue(any(), eq(BinListResponse.class));
        verify(cardInfoRepository, times(1)).save(any(CardInfo.class));
        verifyNoMoreInteractions(webClientCall, objectMapper, cardInfoRepository);
    }

    @Test
    void verifyCard_WithDatabaseException_ShouldThrowDatabaseException()
    {
        String cardNumber = "12345678";

        when(cardInfoRepository.findByCardNumber(cardNumber)).thenThrow(new RuntimeException("Simulated Database Exception"));

        assertThrows(DatabaseException.class, () -> cardInfoService.verifyCard(cardNumber));

        verify(cardInfoRepository, times(1)).findByCardNumber(cardNumber);
        verifyNoMoreInteractions(webClientCall, objectMapper, cardInfoRepository);
    }

    @Test
    void verifyCard_WithConnectionException_ShouldThrowConnectionException()
    {
        String cardNumber = "12345678";

        when(cardInfoRepository.findByCardNumber(cardNumber)).thenReturn(null);
        when(webClientCall.cardServiceApiCall(cardNumber)).thenThrow(new RuntimeException("Simulated Connection Exception"));

        assertThrows(ConnectionException.class, () -> cardInfoService.verifyCard(cardNumber));

        verify(cardInfoRepository, times(1)).findByCardNumber(cardNumber);
        verify(webClientCall, times(1)).cardServiceApiCall(cardNumber);
        verifyNoMoreInteractions(objectMapper, cardInfoRepository);
    }

}