package com.vetri.poc.transaction.dao;

import com.vetri.poc.transaction.model.Employee;

public interface EmployeeDao {
    void insertEmployee(Employee cus);
    void deleteEmployeeById(String empid);
}
