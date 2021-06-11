package com.vetri.poc.transaction.service;

import com.vetri.poc.transaction.model.Employee;
import com.vetri.poc.transaction.model.EmployeeHealthInsurance;
import com.vetri.poc.transaction.service.impl.InvalidInsuranceAmountException;

public interface OrganizationService {

	public void joinOrganization(Employee employee, EmployeeHealthInsurance employeeHealthInsurance)throws InvalidInsuranceAmountException;;

	public void leaveOrganization(Employee employee, EmployeeHealthInsurance employeeHealthInsurance);

}
