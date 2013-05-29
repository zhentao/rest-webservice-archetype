package com.zhentao.rest.resource;

import static javax.ws.rs.core.Response.Status.CONFLICT;
import static javax.ws.rs.core.Response.Status.FORBIDDEN;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

import java.net.URI;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.codahale.metrics.annotation.Timed;
import com.zhentao.rest.exception.EmployeeExistsException;
import com.zhentao.rest.exception.NoSuchEmployeeException;
import com.zhentao.rest.exception.ServiceException;
import com.zhentao.rest.model.Employee;
import com.zhentao.rest.service.EmployeeService;

@Path("/employee")
public class EmployeeResource {

    private static final String EMPLOYEE_NOT_FOUND = "Employee %d does not exist";
    private static final String EMPLOYEE_ALREADY_EXISTS = "Employee %d already exists";
    private static final String PARAMETER_OUT_OF_RANGE = "The parameter %s should be the range %d...%d";
    private static final String PATH_BODY_CONFLICT = "Id %d in path differs from id %d in body";

    private static final int MIN_PAGE_SIZE = 1;
    private static final int MAX_PAGE_SIZE = 50;

    private final EmployeeService employeeService;

    public EmployeeResource(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @POST
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Timed
    public Response createEmployee(final Employee employee, @Context UriInfo uriInfo) {
        try {
            employeeService.createEmployee(employee);
            URI newLocation = uriInfo.getAbsolutePathBuilder().path(String.valueOf(employee.getId())).build();
            return Response.created(newLocation).build();
        } catch (EmployeeExistsException e) {
            throw new ServiceException(CONFLICT, EMPLOYEE_ALREADY_EXISTS, employee.getId());
        }
    }

    @PUT
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/{id}")
    @Timed
    public Response updateEmployee(@PathParam("id") final long id, final Employee employee) {
        checkIdAgreement(id, employee.getId());

        try {
            employeeService.updateEmployee(employee);
            return Response.noContent().build();
        } catch (NoSuchEmployeeException e) {
            throw new ServiceException(NOT_FOUND, EMPLOYEE_NOT_FOUND, id);
        }
    }

    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/{id}")
    @Timed
    public Employee findEmployeeById(@PathParam("id") long id) {
        try {
            return employeeService.findEmployeeById(id);
        } catch (NoSuchEmployeeException e) {
            throw new ServiceException(NOT_FOUND, EMPLOYEE_NOT_FOUND, id);
        }
    }

    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Timed
    public List<Employee> findEmployees(@QueryParam("name") String name, @QueryParam("page") int page,
                                    @QueryParam("pageSize") int pageSize) {
        checkRange("pageSize", pageSize, MIN_PAGE_SIZE, MAX_PAGE_SIZE);

        return employeeService.findEmployees(name, page, pageSize);
    }

    @DELETE
    @Path("/{id}")
    @Timed
    public Response deleteEmployee(@PathParam("id") long id) {
        try {
            Employee e = employeeService.findEmployeeById(id);
            employeeService.deleteEmployee(e);
            return Response.noContent().build();
        } catch (NoSuchEmployeeException ex) {
            throw new ServiceException(NOT_FOUND, EMPLOYEE_NOT_FOUND, id);
        }
    }

    private void checkRange(String name, int value, int low, int high) {
        if (value < low || value > high) {
            throw new ServiceException(FORBIDDEN, PARAMETER_OUT_OF_RANGE, name, low, high);
        }
    }

    private void checkIdAgreement(long idInPath, long idInBody) {
        if (idInPath != idInBody) {
            throw new ServiceException(CONFLICT, PATH_BODY_CONFLICT, idInPath, idInBody);
        }
    }
}
