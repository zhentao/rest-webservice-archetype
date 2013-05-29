package com.zhentao.rest.resource;

import static com.jayway.restassured.RestAssured.expect;

import org.junit.Test;

public class HealthcheckResourceIT {

    @Test
    public void healthcheckProducesHttp200() {
        expect().
            statusCode(200).
        when().
            get("/rest/healthcheck");
    }
}
