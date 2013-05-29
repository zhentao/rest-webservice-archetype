package com.zhentao.rest.config;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("prod")
public class JndiDataSourceConfig implements DataSourceConfig {

    @Override
    public DataSource dataSource() {
        try {
            Context ctx = new InitialContext();
            return (DataSource) ctx.lookup("java:comp/env/jdbc/datasource");
        } catch (NamingException e) {
            throw new BeanInitializationException("error initialize data source", e);
        }
    }
}
