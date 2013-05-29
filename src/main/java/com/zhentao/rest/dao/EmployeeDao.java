package com.zhentao.rest.dao;

import java.util.List;

import com.zhentao.rest.model.Employee;

public interface EmployeeDao {

    void createEmployee(Employee e);

    Employee findEmployeeById(long id);
    List<Employee> findEmployeesByName(String name, int pageNumber, int pageSize);
    List<Employee> findAllEmployees(int pageNumber, int pageSize);

    void updateEmployee(Employee e);

    void deleteEmployee(Employee e);

    int findNumberOfEmployees();
}
