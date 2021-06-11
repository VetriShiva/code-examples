package com.vetri.poc.transaction.service;

import com.vetri.poc.transaction.model.EmployeeHealthInsurance;
import com.vetri.poc.transaction.service.impl.InvalidInsuranceAmountException;

public interface HealthInsuranceService {
	void registerEmployeeHealthInsurance(EmployeeHealthInsurance employeeHealthInsurance)throws InvalidInsuranceAmountException;;

	void deleteEmployeeHealthInsuranceById(String empid);
}