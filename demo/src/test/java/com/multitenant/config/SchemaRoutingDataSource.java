package com.multitenant.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.sql.Connection;

public class SchemaRoutingDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return null;
    }

    @Override
    protected DataSource determineTargetDataSource() {
        String tenantId = EventContext.getTenantId();
        DataSource ds = super.getResolvedDefaultDataSource();
        try (Connection connection = ds.getConnection()) {
            connection.createStatement().execute("SET search_path TO " + tenantId);
        } catch (Exception e) {
            throw new RuntimeException("Could not set schema to " + tenantId, e);
        }
        return ds;
    }
}
