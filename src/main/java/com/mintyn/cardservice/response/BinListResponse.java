package com.mintyn.cardservice.response;

import com.mintyn.cardservice.response.dto.Bank;
import com.mintyn.cardservice.response.dto.Country;
import com.mintyn.cardservice.response.dto.Number;
import lombok.Data;

@Data
public class BinListResponse {

    private Number number;

    private String scheme;

    private String type;

    private String brand;

    private boolean prepaid;

    private Country country;

    private Bank bank;
}
