package com.vetri.poc.transaction.service.impl;

import com.vetri.poc.transaction.model.Employee;
import com.vetri.poc.transaction.model.EmployeeHealthInsurance;
import com.vetri.poc.transaction.service.EmployeeService;
import com.vetri.poc.transaction.service.HealthInsuranceService;
import com.vetri.poc.transaction.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrganzationServiceImpl implements OrganizationService {

	@Autowired
	EmployeeService employeeService;

	@Autowired
	HealthInsuranceService healthInsuranceService;

	@Override
	@Transactional(rollbackFor = InvalidInsuranceAmountException.class)
	//(isolation = Isolation.SERIALIZABLE)
	public void joinOrganization(Employee employee, EmployeeHealthInsurance employeeHealthInsurance) throws InvalidInsuranceAmountException {
		employeeService.insertEmployee(employee);
//		if (employee.getEmpId().equals("emp1")) {
//			throw new RuntimeException("thowing exception to test transaction rollback");
//		}

		try {
			healthInsuranceService.registerEmployeeHealthInsurance(employeeHealthInsurance);
		} catch (InvalidInsuranceAmountException e) {
			throw new InvalidInsuranceAmountException("Exception is thrown");
		}
	}

	@Override
	@Transactional
	public void leaveOrganization(Employee employee, EmployeeHealthInsurance employeeHealthInsurance) {
		employeeService.deleteEmployeeById(employee.getEmpId());
		healthInsuranceService.deleteEmployeeHealthInsuranceById(employeeHealthInsurance.getEmpId());
	}
}