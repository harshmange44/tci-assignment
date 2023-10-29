package com.assignment.tcilabs.service.impl;

import com.assignment.tcilabs.entity.EmployeeBonus;
import com.assignment.tcilabs.payload.EmployeeBonusDto;
import com.assignment.tcilabs.payload.EmployeeBonusRequestBody;
import com.assignment.tcilabs.repository.EmployeeBonusRepository;
import com.assignment.tcilabs.service.TCIService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TCIServiceImplTest {

    @Mock
    EmployeeBonusRepository mockEmployeeBonusRepository;

    @InjectMocks
    TCIServiceImpl underTest;

    @Test
    void createEmployeeBonus() {
        // given
        EmployeeBonusDto expected = new EmployeeBonusDto();
        expected.setEmpName("100th Emp");

        EmployeeBonusRequestBody request = new EmployeeBonusRequestBody();
        List<EmployeeBonusDto> employeeBonusDtoList = new ArrayList<>();
        employeeBonusDtoList.add(new EmployeeBonusDto("100th Emp", "IT", 1000, "USD", "may-27-2022", "may-27-2023"));
        request.setEmployees(employeeBonusDtoList);

        // no employee bonus with this id
        when(mockEmployeeBonusRepository.findById(any())).
                thenReturn(Optional.empty());

        // mocking save method
        when(mockEmployeeBonusRepository.save(any())).thenReturn(expected);

        // when
        EmployeeBonusDto actual = underTest.createEmployeeBonus(request).get(0);

        // then
        assertAll(
                () -> assertNotNull(actual),
                () -> assertEquals(expected, actual),
                () -> assertEquals(expected.getEmpName(), actual.getEmpName())
        );
    }

    @Test
    void getEmployeeBonusById() {
        // given
        EmployeeBonus employeeBonus = new EmployeeBonus();
        employeeBonus.setId(1L);
        employeeBonus.setEmpName("raj singh");
        employeeBonus.setAmount(5000);
        employeeBonus.setCurrency("INR");
        employeeBonus.setDepartment("accounts");
        employeeBonus.setJoiningDate("may-20-2022");
        employeeBonus.setExitDate("may-20-2023");

        EmployeeBonusDto response = new EmployeeBonusDto();
        response.setEmpName(employeeBonus.getEmpName());
        response.setAmount(employeeBonus.getAmount());
        response.setCurrency(employeeBonus.getCurrency());
        response.setDepartment(employeeBonus.getDepartment());
        response.setJoiningDate(employeeBonus.getJoiningDate());
        response.setExitDate(employeeBonus.getExitDate());

        when(mockEmployeeBonusRepository.findById(anyLong())).
                thenReturn(Optional.of(employeeBonus));

        // when
        EmployeeBonusDto actual = underTest.getEmployeeBonusById(1L);

        // then
        assertEquals(response, actual);
    }
}