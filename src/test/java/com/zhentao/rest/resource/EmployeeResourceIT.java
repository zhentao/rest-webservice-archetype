package com.zhentao.rest.resource;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import org.junit.Test;

public class EmployeeResourceIT {

    @Test
    public void endpointGetFindsExistingEmployeeJson() {
        given().
            header("Accept", "application/json").
        expect().
            statusCode(200).
            contentType("application/json").
            body(containsString("\"id\":1")).
        when().
            get("/rest/employee/1");
    }

    @Test
    public void endpointGetFindsExistingEmployeeXML() {
        expect().
            statusCode(200).
            contentType("application/xml").
            body(containsString("<id>1</id>")).
            body(containsString("<employee>")).
        when().
            get("/rest/employee/1");
    }

    @Test
    public void endpointGetFindsAllEmployeeXML() {
        expect().
            statusCode(200).
            contentType("application/xml").
            body(containsString("<id>1</id>")).
            body(containsString("<employee>")).
        when().
            get("/rest/employee?page=0&pageSize=2");
    }

    @Test
    public void endpointGetWithUnusedIdProduces404() {
        expect().
            statusCode(404).
            body(containsString("Employee 100")).
        when().
            get("/rest/employee/100");
    }

    @Test
    public void endpointPutWithUnusedIdProduces404() {
        given().
            header("Accept", "application/json").
            contentType("application/json").
            body("{\"id\":\"100\",\"name\":\"Irina\"}").
        expect().
            statusCode(404).
        when().
            put("/rest/employee/100");
    }

    @Test
    public void endpointPutWithMismatchedIdProduces409() {
        given().
            header("Accept", "application/json").
            contentType("application/json").
            body("{\"id\":\"172\",\"name\":\"Irina\"}").
        expect().
            statusCode(409).
        when().
            put("/rest/employee/46");
    }

    @Test
    public void endpointPostJsonCorrectlyCreatesEmployeeAndProduces201() {
        given().
            header("Accept", "application/json").
            contentType("application/json").
            body("{\"id\":\"9\",\"name\":\"Irina\"}").
        expect().
            statusCode(201).
            header("Location", "http://localhost:8080/rest/employee/9").
            contentType("").
        when().
            post("/rest/employee");
    }
}
