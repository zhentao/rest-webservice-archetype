package com.zhentao.rest.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.zhentao.rest.dao.EmployeeDao;
import com.zhentao.rest.exception.EmployeeExistsException;
import com.zhentao.rest.model.Employee;

public class EmployeeServiceTest {

    EmployeeServiceImpl service;
    EmployeeDao dao;

    Employee sampleEmployee = new Employee(1, "Alice");

    @Before
    public void setUp() {
        dao = mock(EmployeeDao.class);
        service = new EmployeeServiceImpl(dao);
    }

    @Test
    public void creationDelegatesToDao() {
        service.createEmployee(sampleEmployee);
        verify(dao).createEmployee(sampleEmployee);
    }

    @Test(expected=EmployeeExistsException.class)
    public void creationPropagatesExistExceptions() {
        doThrow(new EmployeeExistsException()).when(dao).createEmployee(sampleEmployee);
        service.createEmployee(sampleEmployee);
    }

    @Test
    public void updatesDelegateToDao() {
        service.updateEmployee(sampleEmployee);
        verify(dao).updateEmployee(sampleEmployee);
    }

    @Test
    public void findingDelgatesToDao() {
        when(dao.findEmployeeById(7)).thenReturn(sampleEmployee);
        assertThat(service.findEmployeeById(7), equalTo(sampleEmployee));

        service.findEmployees("Alice", 1, 5);
        verify(dao).findEmployeesByName("Alice", 1, 5);

        service.findEmployees(null, 10, 50);
        verify(dao).findAllEmployees(10, 50);
    }

    @Test
    public void deletionDelegatesToDao() {
        service.deleteEmployee(sampleEmployee);
        verify(dao).deleteEmployee(sampleEmployee);
    }
}
