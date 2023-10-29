package com.assignment.tcilabs.payload;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeBonusDto {

    @NotEmpty
    @Size(min = 2, message = "Employee name should have at least 2 characters")
    private String empName;

    @NotNull
    private String department;

    @NotNull
    private int amount;

    @NotNull
    private String currency;

    @NotNull
    private String joiningDate;

    @NotNull
    private String exitDate;
}
