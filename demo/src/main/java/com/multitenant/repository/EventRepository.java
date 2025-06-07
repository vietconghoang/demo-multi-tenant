package com.multitenant.repository;

import jakarta.annotation.Resource;
import org.postgresql.PGConnection;
import org.postgresql.copy.CopyManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;

@Repository
public class EventRepository {

    @Resource
    private DataSource dataSource;

    public void copyCsvToDatabase(String tenantId, MultipartFile file) throws Exception {
        Connection connection = DataSourceUtils.getConnection(dataSource);

        try {
            connection.createStatement().execute("SET search_path TO " + tenantId);

            PGConnection pgConnection = connection.unwrap(PGConnection.class);
            CopyManager copyManager = pgConnection.getCopyAPI();

            try (InputStream inputStream = file.getInputStream()) {
                String copySql = "COPY event(timestamp, event_number) FROM STDIN WITH (FORMAT csv, HEADER true)";
                copyManager.copyIn(copySql, inputStream);
            }

        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }
}
