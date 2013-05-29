package com.zhentao.rest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.jersey.InstrumentedResourceMethodDispatchAdapter;
import com.zhentao.rest.dao.EmployeeDao;
import com.zhentao.rest.dao.EmployeeDaoJdbcImpl;
import com.zhentao.rest.resource.EmployeeResource;
import com.zhentao.rest.resource.HealthcheckResource;
import com.zhentao.rest.service.EmployeeService;
import com.zhentao.rest.service.EmployeeServiceImpl;

@Configuration
@PropertySource(value = { "file:${PROPERTIES_FILE}" })
@EnableTransactionManagement
public class SpringConfig {

    @Autowired DataSourceConfig dataSourceConfig;

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        // search local properties last by default
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public PlatformTransactionManager txManager() {
        return new DataSourceTransactionManager(dataSourceConfig.dataSource());
    }

    @Bean
    public EmployeeDao employeeDao() {
        EmployeeDaoJdbcImpl employeeDao = new EmployeeDaoJdbcImpl();
        employeeDao.setDataSource(dataSourceConfig.dataSource());
        return employeeDao;
    }

    @Bean
    public EmployeeService employeeService() {
        return new EmployeeServiceImpl(employeeDao());
    }

    @Bean
    public EmployeeResource employeeResource() {
        return new EmployeeResource(employeeService());
    }

    @Bean
    public HealthcheckResource healthcheckResource() {
        return new HealthcheckResource();
    }

    @Bean
    public MetricRegistry metricRegistry() {
        MetricRegistry metrics = new MetricRegistry();
        JmxReporter.forRegistry(metrics).build().start();
        return metrics;
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public InstrumentedResourceMethodDispatchAdapter jerseyAdapter() {
        return new InstrumentedResourceMethodDispatchAdapter(metricRegistry());
    }
}
