package com.zhentao.rest.config;

import org.junit.Test;

public class SpringConfigTest {
    @Test
    public void test() {
        SpringConfig.placeholderConfigurer();

        SpringConfig config = new SpringConfig();
        config.dataSourceConfig = new EmbeddedDataSourceConfig();
        config.txManager();
        config.employeeResource();
        config.healthcheckResource();
        config.jerseyAdapter();
    }
}
