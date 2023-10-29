package com.assignment.tcilabs.controller;

import com.assignment.tcilabs.payload.EmployeeBonusDto;
import com.assignment.tcilabs.payload.EmployeeBonusRequestBody;
import com.assignment.tcilabs.payload.EmployeeBonusResponse;
import com.assignment.tcilabs.payload.ValidEmployeeBonusByCurrencyResponse.ValidEmployeeBonusByCurrencyResponse;
import com.assignment.tcilabs.service.TCIService;
import com.assignment.tcilabs.utils.AppConstants;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tci")
public class TCIController {

    private final TCIService tciService;

    @Autowired
    public TCIController(TCIService tciService) {
        this.tciService = tciService;
    }

    // insert 'employee bonus' record rest api
    @PostMapping("/employee-bonus")
    public ResponseEntity<List<EmployeeBonusDto>> createEmployeeBonus(@Valid @RequestBody EmployeeBonusRequestBody employeeBonusRequestBody){
        return new ResponseEntity<>(tciService.createEmployeeBonus(employeeBonusRequestBody), HttpStatus.CREATED);
    }

    // get all Employee Bonus rest api
    @GetMapping("/employee-bonus/all")
    public EmployeeBonusResponse getAllEmployeeBonus(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ){
        return tciService.getAllEmployeeBonus(pageNo, pageSize, sortBy, sortDir);
    }

    // get all Valid Employee Bonus By Date rest api
    @GetMapping("/employee-bonus")
    public ValidEmployeeBonusByCurrencyResponse getAllEmployeeBonusByDate(
            @RequestParam(value = "date") String date
    ){
        return tciService.getValidEmployeeBonusByDate(date);
    }
}
