package com.assignment.tcilabs.repository;

import com.assignment.tcilabs.entity.EmployeeBonus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeBonusRepository extends JpaRepository<EmployeeBonus, Long> {
}