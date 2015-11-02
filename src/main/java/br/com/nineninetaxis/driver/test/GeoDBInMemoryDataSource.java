package br.com.nineninetaxis.driver.test;

import geodb.GeoDB;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class GeoDBInMemoryDataSource extends SingleConnectionDataSource {

    public GeoDBInMemoryDataSource() {
        setDriverClassName("org.h2.Driver");
        setUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=TRUE");
        setSuppressClose(true);
    }

    @Override
    public Connection getConnection() throws SQLException {
        Connection conn = super.getConnection();
        GeoDB.InitGeoDB(conn);
        return conn;
    }
}