package com.vetri.poc.transaction.service;

import com.vetri.poc.transaction.model.Employee;

public interface EmployeeService {
	void insertEmployee(Employee emp);

	void deleteEmployeeById(String empid);
}