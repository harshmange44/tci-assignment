package com.assignment.tcilabs.service;

import com.assignment.tcilabs.payload.EmployeeBonusDto;
import com.assignment.tcilabs.payload.EmployeeBonusRequestBody;
import com.assignment.tcilabs.payload.EmployeeBonusResponse;
import com.assignment.tcilabs.payload.ValidEmployeeBonusByCurrencyResponse.ValidEmployeeBonusByCurrencyResponse;

import java.util.List;

public interface TCIService {
    List<EmployeeBonusDto> createEmployeeBonus(EmployeeBonusRequestBody employeeBonusRequestBody);

    ValidEmployeeBonusByCurrencyResponse getValidEmployeeBonusByDate(String date);

    EmployeeBonusResponse getAllEmployeeBonus(int pageNo, int pageSize, String sortBy, String sortDir);

    EmployeeBonusDto getEmployeeBonusById(long id);
}
