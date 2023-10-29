package com.assignment.tcilabs.service.impl;

import com.assignment.tcilabs.entity.EmployeeBonus;
import com.assignment.tcilabs.exception.ResourceNotFoundException;
import com.assignment.tcilabs.exception.TCIAPIException;
import com.assignment.tcilabs.payload.EmployeeBonusDto;
import com.assignment.tcilabs.payload.EmployeeBonusRequestBody;
import com.assignment.tcilabs.payload.EmployeeBonusResponse;
import com.assignment.tcilabs.payload.ValidEmployeeBonusByCurrencyResponse.ValidEmployeeBonusAmount;
import com.assignment.tcilabs.payload.ValidEmployeeBonusByCurrencyResponse.ValidEmployeeBonusByCurrencyResponse;
import com.assignment.tcilabs.payload.ValidEmployeeBonusByCurrencyResponse.ValidEmployeeBonusCurrency;
import com.assignment.tcilabs.repository.EmployeeBonusRepository;
import com.assignment.tcilabs.service.TCIService;
import com.assignment.tcilabs.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TCIServiceImpl implements TCIService {

    private final EmployeeBonusRepository employeeBonusRepository;
    private final ModelMapper mapper = new ModelMapper();

    @Autowired
    public TCIServiceImpl(EmployeeBonusRepository employeeBonusRepository) {
          this.employeeBonusRepository = employeeBonusRepository;
    }

    @Override
    public List<EmployeeBonusDto> createEmployeeBonus(EmployeeBonusRequestBody employeeBonusRequestBody) {

        List<EmployeeBonusDto> createdEmployeeBonusResponseList = new ArrayList<>();
        // convert DTO to entity
        employeeBonusRequestBody.getEmployees().forEach(employeeBonusDto -> {
            EmployeeBonus employeeBonus = mapToEntity(employeeBonusDto);
            EmployeeBonus createdEmployeeBonus = employeeBonusRepository.save(employeeBonus);
            createdEmployeeBonusResponseList.add(mapToDTO(createdEmployeeBonus));
        });

        return createdEmployeeBonusResponseList;
    }

    @Override
    public ValidEmployeeBonusByCurrencyResponse getValidEmployeeBonusByDate(String requestedDateStr) {

        ValidEmployeeBonusByCurrencyResponse validEmployeeBonusByCurrencyResponse = new ValidEmployeeBonusByCurrencyResponse();

        List<EmployeeBonus> employeeBonuses = employeeBonusRepository.findAll();

        String pattern = "MMM-dd-yyyy";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        Date requestedDate;
        try {
            requestedDate = formatter.parse(CommonUtils.capitalizeFirstLetter(requestedDateStr));
        } catch (ParseException e) {
            log.error("Error while extracting date: {}", requestedDateStr);
            throw new TCIAPIException(HttpStatus.BAD_REQUEST, String.format("Error while extracting date: %s", requestedDateStr));
        }

        employeeBonuses = employeeBonuses.stream()
                .filter(employeeBonus -> {
                    try {
                        return formatter.parse(
                                CommonUtils.capitalizeFirstLetter(employeeBonus.getJoiningDate()))
                                .before(requestedDate)
                                && formatter.parse(CommonUtils.capitalizeFirstLetter(employeeBonus.getExitDate()))
                                .after(requestedDate);
                    } catch (ParseException e) {
                        log.error("Error while extracting date for employee: {}", employeeBonus.getEmpName());
                        throw new TCIAPIException(HttpStatus.BAD_REQUEST, String.format("Error while extracting date for employee: %s", employeeBonus.getEmpName()));
                    }
                })
                .collect(Collectors.toList());

        Map<String, List<ValidEmployeeBonusAmount>> validEmployeeBonusAmountMap = new HashMap<>();

        employeeBonuses.forEach(employeeBonus -> {
                log.debug("Extracted eligible employees: {}", employeeBonus.toString());
                List<ValidEmployeeBonusAmount> validEmployeeBonusAmountList = validEmployeeBonusAmountMap
                        .getOrDefault(employeeBonus.getCurrency(), new ArrayList<>());
                validEmployeeBonusAmountList.add(new ValidEmployeeBonusAmount(employeeBonus.getEmpName()
                        , employeeBonus.getAmount()));
                validEmployeeBonusAmountMap.put(employeeBonus.getCurrency(), validEmployeeBonusAmountList);
            }
        );

        List<ValidEmployeeBonusCurrency> validEmployeeBonusCurrencyList = new ArrayList<>();
        validEmployeeBonusAmountMap.forEach((currency, validEmployeeBonusAmounts) -> {
            validEmployeeBonusAmounts.sort((e1, e2) -> e1.getEmpName().compareToIgnoreCase(e2.getEmpName()));
            validEmployeeBonusCurrencyList.add(new ValidEmployeeBonusCurrency(currency, validEmployeeBonusAmounts));
        });

        validEmployeeBonusByCurrencyResponse.setErrorMessage("");
        validEmployeeBonusByCurrencyResponse.setData(validEmployeeBonusCurrencyList);
        return validEmployeeBonusByCurrencyResponse;
    }

    @Override
    public EmployeeBonusResponse getAllEmployeeBonus(int pageNo, int pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<EmployeeBonus> employeeBonuses = employeeBonusRepository.findAll(pageable);

        // get content for page object
        List<EmployeeBonus> employeeBonusList = employeeBonuses.getContent();

        List<EmployeeBonusDto> content= employeeBonusList.stream().map(this::mapToDTO).collect(Collectors.toList());

        EmployeeBonusResponse employeeBonusResponse = new EmployeeBonusResponse();
        employeeBonusResponse.setContent(content);
        employeeBonusResponse.setPageNo(employeeBonuses.getNumber());
        employeeBonusResponse.setPageSize(employeeBonuses.getSize());
        employeeBonusResponse.setTotalElements(employeeBonuses.getTotalElements());
        employeeBonusResponse.setTotalPages(employeeBonuses.getTotalPages());
        employeeBonusResponse.setLast(employeeBonuses.isLast());

        return employeeBonusResponse;
    }

    @Override
    public EmployeeBonusDto getEmployeeBonusById(long id) {
        EmployeeBonus employeeBonus = employeeBonusRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("EmployeeBonus", "id", id));
        return mapToDTO(employeeBonus);
    }

    // convert Entity into DTO
    private EmployeeBonusDto mapToDTO(EmployeeBonus employeeBonus){
        return mapper.map(employeeBonus, EmployeeBonusDto.class);
    }

    // convert DTO to entity
    private EmployeeBonus mapToEntity(EmployeeBonusDto employeeBonusDto){
        return mapper.map(employeeBonusDto, EmployeeBonus.class);
    }
}
