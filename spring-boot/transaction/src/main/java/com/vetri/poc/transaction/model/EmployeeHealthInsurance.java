package com.vetri.poc.transaction.model;

import lombok.Data;

@Data
public class EmployeeHealthInsurance {
    private String empId;
    private String healthInsuranceSchemeName;
    private int coverageAmount;
}
