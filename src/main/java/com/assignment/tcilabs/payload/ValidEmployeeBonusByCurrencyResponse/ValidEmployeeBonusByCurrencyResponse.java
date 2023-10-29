package com.assignment.tcilabs.payload.ValidEmployeeBonusByCurrencyResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidEmployeeBonusByCurrencyResponse {
    private String errorMessage;
    private List<ValidEmployeeBonusCurrency> data;
}

