package com.zhentao.rest.resource;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class HealthcheckResourceTest {

    HealthcheckResource resource = new HealthcheckResource();

    @Test
    public void healthCheckProducesHttp200() {
        assertThat(resource.healthcheck().getStatus(), is(200));
    }
}
