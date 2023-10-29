package com.assignment.tcilabs.payload.ValidEmployeeBonusByCurrencyResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidEmployeeBonusAmount {
    private String empName;
    private int amount;
}
