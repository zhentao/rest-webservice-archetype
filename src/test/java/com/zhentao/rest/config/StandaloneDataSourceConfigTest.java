package com.zhentao.rest.config;

import org.junit.Test;

public class StandaloneDataSourceConfigTest {
    @Test
    public void testDataSource() {
        StandaloneDataSourceConfig config = new StandaloneDataSourceConfig();
        config.dataSource();
    }
}
