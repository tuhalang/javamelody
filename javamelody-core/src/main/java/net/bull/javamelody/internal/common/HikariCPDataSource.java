package net.bull.javamelody.internal.common;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.bull.javamelody.Parameter;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author hungpv
 * @date 17/02/2020
 */
public class HikariCPDataSource {
    private HikariDataSource ds;
    private static HikariCPDataSource hikariCPDataSource;

    public static HikariCPDataSource getInstance(){
        if(hikariCPDataSource != null) {
        }
        else {
            hikariCPDataSource = new HikariCPDataSource();
        }
        return hikariCPDataSource;
    }

    private HikariCPDataSource(){
        final String url = Parameter.DATABASE_URL.getValue();
        final String username = Parameter.DATABASE_USERNAME.getValue();
        final String password = Parameter.DATABASE_PASSWORD.getValue();
        final String driver = Parameter.DATABASE_DRIVER.getValue();
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(driver);
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        this.ds = new HikariDataSource(config);
    }

    public Connection getConnection() throws SQLException {
        return this.ds.getConnection();
    }


}
