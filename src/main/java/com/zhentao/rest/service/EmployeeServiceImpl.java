package com.zhentao.rest.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.zhentao.rest.dao.EmployeeDao;
import com.zhentao.rest.model.Employee;

@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeDao employeeDao;

    public EmployeeServiceImpl(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    @Override
    public void createEmployee(Employee e) {
        employeeDao.createEmployee(e);
    }

    @Override
    public void updateEmployee(Employee e) {
        employeeDao.updateEmployee(e);
    }

    @Override
    public Employee findEmployeeById(long id) {
        return employeeDao.findEmployeeById(id);
    }

    @Override
    public List<Employee> findEmployees(String name, int pageNumber, int pageSize) {
        return name == null ? employeeDao.findAllEmployees(pageNumber, pageSize) : employeeDao.findEmployeesByName(
                name, pageNumber, pageSize);
    }

    @Override
    public void deleteEmployee(Employee e) {
        employeeDao.deleteEmployee(e);
    }
}
