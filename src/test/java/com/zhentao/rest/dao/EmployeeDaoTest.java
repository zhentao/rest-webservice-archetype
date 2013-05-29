package com.zhentao.rest.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;

import com.zhentao.rest.config.EmbeddedDataSourceConfig;
import com.zhentao.rest.exception.EmployeeExistsException;
import com.zhentao.rest.exception.NoSuchEmployeeException;
import com.zhentao.rest.model.Employee;

/**
 * Unit tests on the JDBC Dao using a programmatically-configured embedded database.  The database is setup and torn
 * down around each test so that the tests don't affect each other.
 */
public class EmployeeDaoTest {

    private EmbeddedDatabase database;
    private final EmployeeDaoJdbcImpl employeeDao = new EmployeeDaoJdbcImpl();

    @Before
    public void setUp() {
        database = new EmbeddedDataSourceConfig().dataSource();
        employeeDao.setDataSource(database);
    }

    @Test
    public void creatingIncrementsSize() {
        Employee e = new Employee(9, "Imogen");

        int initialCount = employeeDao.findNumberOfEmployees();
        employeeDao.createEmployee(e);
        assertThat(employeeDao.findNumberOfEmployees(), is(initialCount + 1));
    }

    @Test
    public void deletingDecrementsSize() {
        Employee e = new Employee(1, "Alice");

        int initialCount = employeeDao.findNumberOfEmployees();
        employeeDao.deleteEmployee(e);
        assertThat(employeeDao.findNumberOfEmployees(), is(initialCount - 1));
    }

    @Test
    public void createdEmployeeCanBeFound() {
        employeeDao.createEmployee(new Employee(9, "Imogen"));
        Employee e = employeeDao.findEmployeeById(9);
        assertThat(e.getId(), is(9L));
        assertThat(e.getName(), is("Imogen"));
    }

    @Test
    public void updatesToCreatedEmployeeCanBeRead() {
        long id = 9;
        employeeDao.createEmployee(new Employee(id, "Imogen"));
        Employee e = employeeDao.findEmployeeById(id);
        e.setName("Irina");
        employeeDao.updateEmployee(e);
        e = employeeDao.findEmployeeById(id);
        assertThat(e.getId(), is(id));
        assertThat(e.getName(), is("Irina"));
    }

    @Test(expected=EmployeeExistsException.class)
    public void creatingDuplicateEmployeeThrowsException() {
        employeeDao.createEmployee(new Employee(1, "Id1WasAlreadyUsed"));
    }

    @Test(expected=NoSuchEmployeeException.class)
    public void updatingNonExistentEmployeeThrowsException() {
        employeeDao.updateEmployee(new Employee(1000, "Unknown"));
    }

    @Test(expected=NoSuchEmployeeException.class)
    public void deletingNonExistentEmployeeThrowsException() {
        employeeDao.deleteEmployee(new Employee(1000, "Unknown"));
    }

    @Test(expected=NoSuchEmployeeException.class)
    public void findingNonExistentEmployeeThrowsException() {
        employeeDao.findEmployeeById(1000);
    }

    @Test
    public void countOfInitialDataSetIsAsExpected() {
        assertThat(employeeDao.findNumberOfEmployees(), is(8));
    }

    @Test
    public void employeeAliceIsInInitialDataSet() {
        List<Employee> employees = employeeDao.findEmployeesByName("Alice", 0, 12);
        assertThat(employees.size(), is(1));

    }

    @Test
    public void findingByNonexistentNameReturnsEmptyList() {
        List<Employee> employees = employeeDao.findEmployeesByName("Qwertyuiop", 0, 10);
        assertThat(employees.size(), is(0));
    }

    @Test
    public void findingEmployeesViaPaginationWorks() {
        assertThat(employeeDao.findNumberOfEmployees(), is(8));
        List<Employee> employees = employeeDao.findAllEmployees(0, 3);
        assertThat(employees.size(), is(3));
        employees = employeeDao.findAllEmployees(1, 3);
        assertThat(employees.size(), is(3));
        employees = employeeDao.findAllEmployees(2, 3);
        assertThat(employees.size(), is(2));
    }

    @After
    public void tearDownDatabase() {
        database.shutdown();
    }
}
