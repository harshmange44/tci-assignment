package com.assignment.tcilabs.payload.ValidEmployeeBonusByCurrencyResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidEmployeeBonusCurrency {
    private String currency;
    private List<ValidEmployeeBonusAmount> employees;
}
