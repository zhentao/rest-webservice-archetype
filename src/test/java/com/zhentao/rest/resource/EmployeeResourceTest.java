package com.zhentao.rest.resource;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.zhentao.rest.exception.EmployeeExistsException;
import com.zhentao.rest.exception.NoSuchEmployeeException;
import com.zhentao.rest.exception.ServiceException;
import com.zhentao.rest.model.Employee;
import com.zhentao.rest.service.EmployeeService;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeResourceTest {

    private EmployeeResource resource;
    @Mock
    private EmployeeService service;
    @Mock
    private UriInfo sampleUriInfo;

    Employee sampleEmployee = new Employee(1, "Alice");



    @Before
    public void setUp() {
        resource = new EmployeeResource(service);

        UriBuilder uriBuilder = UriBuilder.fromUri("http://example.com");
        when(sampleUriInfo.getAbsolutePathBuilder()).thenReturn(uriBuilder);
    }

    @Test
    public void employeeCreationCreatesEmployeeWithLocationHeader() {
        Response response = resource.createEmployee(sampleEmployee, sampleUriInfo);
        verify(service).createEmployee(sampleEmployee);
        assertThat(response.getMetadata().getFirst("Location").toString(), is("http://example.com/1"));
        assertThat(response.getStatus(), is(201));
    }

    @Test
    public void creatingDuplicateEmployeeThrowsException() {
        try {
            doThrow(new EmployeeExistsException()).when(service).createEmployee(sampleEmployee);
            resource.createEmployee(sampleEmployee, sampleUriInfo);
            fail();
        } catch (ServiceException e) {
            assertThat(e.getResponse().getStatus(), is(409));
        }
    }

    @Test
    public void updatingEmployeeProducesHttp204() {
        Response response = resource.updateEmployee(1, sampleEmployee);
        verify(service).updateEmployee(sampleEmployee);
        assertThat(response.getStatus(), is(204));
    }

    @Test
    public void updatingEmployeeWithMismatchedIdThrowsException() {
        try {
            resource.updateEmployee(7, sampleEmployee);
            fail();
        } catch (ServiceException e) {
            assertThat(e.getResponse().getStatus(), is(409));
        }
    }

    @Test
    public void updatingNonexistingEmployeeThrowsException() {
        try {
            doThrow(new NoSuchEmployeeException()).when(service).updateEmployee(sampleEmployee);
            resource.updateEmployee(1, sampleEmployee);
            fail();
        } catch (ServiceException e) {
            assertThat(e.getResponse().getStatus(), is(404));
        }
    }

    @Test
    public void deletingEmployeeProducesHttp204() {
        when(service.findEmployeeById(1)).thenReturn(sampleEmployee);
        Response response = resource.deleteEmployee(1);
        verify(service).deleteEmployee(sampleEmployee);
        assertThat(response.getStatus(), is(204));
    }

    @Test
    public void deletingNonexistingEmployeeThrowsException() {
        try {
            when(service.findEmployeeById(1)).thenReturn(sampleEmployee);
            doThrow(new NoSuchEmployeeException()).when(service).deleteEmployee(sampleEmployee);
            resource.deleteEmployee(1);
            fail();
        } catch (ServiceException e) {
            assertThat(e.getResponse().getStatus(), is(404));
        }
    }

    @Test
    public void findingExistingEmployeeByIdProducesHttp200() {
        when(service.findEmployeeById(1)).thenReturn(sampleEmployee);
        resource.findEmployeeById(1);
        verify(service).findEmployeeById(1);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = ServiceException.class)
    public void findingByIdWithNonExistingIdThrowsException() {
        when(service.findEmployeeById(-1)).thenThrow(NoSuchEmployeeException.class);
        resource.findEmployeeById(-1);
    }

    @Test
    public void findingByNameReturnsList() {
        List<Employee> sampleEmployeeList = new ArrayList<Employee>();
        when(service.findEmployees("Alice", 1, 10)).thenReturn(sampleEmployeeList);
        List<Employee> result = resource.findEmployees("Alice", 1, 10);
        assertThat(result, is(sampleEmployeeList));
    }

    @Test
    public void findingByNameWithPageSizeTooHighProducesHttp403() {
        try {
            resource.findEmployees("Alice", 1, 51);
            fail();
        } catch (ServiceException e) {
            assertThat(e.getResponse().getStatus(), is(403));
        }
    }

    @Test
    public void findingByNameWithPageSizeTooLowProducesHttp403() {
        try {
            resource.findEmployees("Alice", 0, 0);
            fail();
        } catch (ServiceException e) {
            assertThat(e.getResponse().getStatus(), is(403));
        }
    }
}
