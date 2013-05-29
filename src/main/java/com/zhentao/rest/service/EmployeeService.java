package com.zhentao.rest.service;

import java.util.List;

import com.zhentao.rest.model.Employee;

public interface EmployeeService {

    void createEmployee(Employee e);

    void updateEmployee(Employee e);

    Employee findEmployeeById(long id);

    List<Employee> findEmployees(String name, int pageNumber, int pageSize);

    void deleteEmployee(Employee e);
}
