package com.assignment.tcilabs.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@Entity
@Table(
        name = "employeebonus"
)
public class EmployeeBonus {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private long id;

    @Column(name = "empName", nullable = false)
    private String empName;

    @Column(name = "department", nullable = false)
    private String department;

    @Column(name = "amount", nullable = false)
    private int amount;

    @Column(name = "currency", nullable = false)
    private String currency;

    @Column(name = "joiningDate", nullable = false)
    private String joiningDate;

    @Column(name = "exitDate", nullable = false)
    private String exitDate;
}
