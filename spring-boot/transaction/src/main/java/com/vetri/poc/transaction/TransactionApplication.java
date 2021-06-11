package com.vetri.poc.transaction;

import com.vetri.poc.transaction.model.Employee;
import com.vetri.poc.transaction.model.EmployeeHealthInsurance;
import com.vetri.poc.transaction.service.EmployeeService;
import com.vetri.poc.transaction.service.OrganizationService;
import com.vetri.poc.transaction.service.impl.InvalidInsuranceAmountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class TransactionApplication {

	@Autowired
	EmployeeService employeeService;

	public static void main(String[] args) throws InvalidInsuranceAmountException {
		ApplicationContext context = SpringApplication.run(TransactionApplication.class, args);
		OrganizationService organizationService = context.getBean(OrganizationService.class);

		Employee emp = new Employee();
		emp.setEmpId("emp1");
		emp.setEmpName("emp1");

		EmployeeHealthInsurance employeeHealthInsurance = new EmployeeHealthInsurance();
		employeeHealthInsurance.setEmpId("emp1");
		employeeHealthInsurance.setHealthInsuranceSchemeName("premium");
		employeeHealthInsurance.setCoverageAmount(0);

		organizationService.joinOrganization(emp, employeeHealthInsurance);

	}
}
