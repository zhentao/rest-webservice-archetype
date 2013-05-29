package com.zhentao.rest.config;

import org.junit.Test;
import org.springframework.beans.factory.BeanInitializationException;

public class JndiDataSourceConfigTest {
    @Test(expected = BeanInitializationException.class)
    public void testDataSource() {
        JndiDataSourceConfig config = new JndiDataSourceConfig();
        config.dataSource();
    }
}
