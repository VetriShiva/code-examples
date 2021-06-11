package com.vetri.poc.transaction.dao;

import com.vetri.poc.transaction.model.EmployeeHealthInsurance;

public interface HealthInsuranceDao {
    void registerEmployeeHealthInsurance(EmployeeHealthInsurance employeeHealthInsurance);
    void deleteEmployeeHealthInsuranceById(String empid);
}
