package com.zhentao.rest.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.zhentao.rest.exception.EmployeeExistsException;
import com.zhentao.rest.exception.NoSuchEmployeeException;
import com.zhentao.rest.model.Employee;

public class EmployeeDaoJdbcImpl extends JdbcDaoSupport implements EmployeeDao {

    private static final String CREATE_EMPLOYEE_SQL = "insert into employee (id, name) values (?,?)";
    private static final String UPDATE_EMPLOYEE_SQL = "update employee set name=? where id=?";
    private static final String FIND_EMPLOYEE_BY_ID_SQL = "select id, name from employee where id=?";
    private static final String FIND_EMPLOYEE_BY_NAME_SQL = "select id, name from employee where name=? limit ? offset ?";
    private static final String FIND_EMPLOYEES_SQL = "select id, name from employee limit ? offset ?";
    private static final String DELETE_EMPLOYEE_SQL = "delete from employee where id=?";
    private static final String COUNT_EMPLOYEES_SQL = "select count(*) from employee";

    @Override
    public void createEmployee(Employee e) {
        try {
            getJdbcTemplate().update(CREATE_EMPLOYEE_SQL, e.getId(), e.getName());
        } catch (DuplicateKeyException ex) {
            throw new EmployeeExistsException();
        }
    }

    @Override
    public void updateEmployee(Employee e) {
        int rowsUpdated = getJdbcTemplate().update(UPDATE_EMPLOYEE_SQL, e.getName(), e.getId());
        if (rowsUpdated == 0) {
            throw new NoSuchEmployeeException();
        }
    }

    @Override
    public Employee findEmployeeById(long id) {
        try {
            return getJdbcTemplate().queryForObject(FIND_EMPLOYEE_BY_ID_SQL, new Object[]{id}, EMEPLOYEE_ROW_MAPPER);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new NoSuchEmployeeException();
        }
    }

    @Override
    public List<Employee> findEmployeesByName(String name, int pageNumber, int pageSize) {
        return getJdbcTemplate().query(FIND_EMPLOYEE_BY_NAME_SQL, new Object[]{name, pageSize, pageNumber * pageSize},
                EMEPLOYEE_ROW_MAPPER);
    }

    @Override
    public List<Employee> findAllEmployees(int pageNumber, int pageSize) {
        return getJdbcTemplate().query(FIND_EMPLOYEES_SQL, new Object[]{pageSize, pageNumber * pageSize}, EMEPLOYEE_ROW_MAPPER);
    }

    @Override
    public void deleteEmployee(Employee e) {
        int rowsUpdated = getJdbcTemplate().update(DELETE_EMPLOYEE_SQL, e.getId());
        if (rowsUpdated == 0) {
            throw new NoSuchEmployeeException();
        }
    }

    @Override
    public int findNumberOfEmployees() {
        return getJdbcTemplate().queryForObject(COUNT_EMPLOYEES_SQL, Integer.class);
    }

    private static final RowMapper<Employee> EMEPLOYEE_ROW_MAPPER = new RowMapper<Employee>() {
        @Override
        public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Employee(rs.getInt("id"), rs.getString("name"));
        }
    };
}
