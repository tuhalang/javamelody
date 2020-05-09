package net.bull.javamelody.internal.publish;

import net.bull.javamelody.Parameter;
import net.bull.javamelody.internal.common.HikariCPDataSource;
import net.bull.javamelody.internal.common.LOG;

import java.io.IOException;
import java.sql.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hungpv
 * @date 17/02/2020
 */

public class Database extends MetricsPublisher {

    final private String sqlInsert = "INSERT INTO " +
            "MONITORING(USE_MEMORY,CPU,HTTP_SESSIONS,ACTIVE_THREADS,ACTIVE_CONNECTIONS,USED_CONNECTIONS,GC,THREAD_COUNT," +
            "LOADED_CLASSES_COUNT,USED_BUFFERED_MEMORY,USED_NON_HEAP_MEMORY,USED_PHYSICAL_MEMORY_SIZE,USED_SWAP_SPACE_SIZE," +
            "SYSTEM_LOAD,SYSTEM_CPU_LOAD,FILE_DESCRIPTORS,HTTP_SESSIONS_MEAN_AGE,TRANSACTIONS_RATE,FREE_DISK_SPACE," +
            "USABLE_DISK_SPACE,TOMCAT_BUSY_THREADS,TOMCAT_BYTES_RECEIVED,TOMCAT_BYTES_SENT,HTTP_HITS_RATE,SQL_HITS_RATE,HTTP_MEAN_TIMES," +
            "HTTP_SYSTEM_ERRORS,SQL_MEAN_TIMES,SQL_SYSTEM_ERRORS,TIME_CAPTURE)" +
            " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private Map<String, Float> properties;

    Database() {
        this.properties = new HashMap<String, Float>();
    }

    public static Database getInstance(String contextPath, String hosts) {
        final Boolean isWriteDatabase = Parameter.WRITE_DATABASE.getValueAsBoolean();

        if (isWriteDatabase){
            return new Database();
        }

        return null;
    }

    @Override
    public void addValue(String metric, double value) throws IOException {
        properties.put(metric, (float) value);
    }

    @Override
    public void send() throws IOException {
        for(String key : properties.keySet()){
            LOG.info(key + ": " + properties.get(key));
        }
        LOG.info("====== START WRITE ON DATABASE =====");
        Connection connection = null;
        try {
            HikariCPDataSource hikariCPDataSource = HikariCPDataSource.getInstance();
            connection = hikariCPDataSource.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement preStatement = connection.prepareStatement(this.sqlInsert);
            setParameter(preStatement);
            preStatement.executeUpdate();
            connection.commit();
            LOG.info("====== WRITE SUCCESSFULLY ! ======");
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                LOG.warn("",ex);
            }
            LOG.warn("",e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                LOG.warn("",e);
            }
        }
    }

    private void setParameter(PreparedStatement preStatement) throws SQLException {
        if (this.properties.containsKey("usedMemory")) {
            preStatement.setFloat(1, this.properties.get("usedMemory"));
        } else {
            preStatement.setFloat(1, 0.0f);
        }

        if (this.properties.containsKey("cpu")) {
            preStatement.setFloat(2, this.properties.get("cpu"));
        } else {
            preStatement.setFloat(2, 0.0f);
        }

        if (this.properties.containsKey("httpSessions")) {
            preStatement.setFloat(3, this.properties.get("httpSessions"));
        } else {
            preStatement.setFloat(3, 0.0f);
        }

        if (this.properties.containsKey("activeThreads")) {
            preStatement.setFloat(4, this.properties.get("activeThreads"));
        } else {
            preStatement.setFloat(4, 0.0f);
        }

        if (this.properties.containsKey("activeConnections")) {
            preStatement.setFloat(5, this.properties.get("activeConnections"));
        } else {
            preStatement.setFloat(5, 0.0f);
        }

        if (this.properties.containsKey("usedConnections")) {
            preStatement.setFloat(6, this.properties.get("usedConnections"));
        } else {
            preStatement.setFloat(6, 0.0f);
        }

        if (this.properties.containsKey("gc")) {
            preStatement.setFloat(7, this.properties.get("gc"));
        } else {
            preStatement.setFloat(7, 0.0f);
        }

        if (this.properties.containsKey("threadCount")) {
            preStatement.setFloat(8, this.properties.get("threadCount"));
        } else {
            preStatement.setFloat(8, 0.0f);
        }

        if (this.properties.containsKey("loadedClassesCount")) {
            preStatement.setFloat(9, this.properties.get("loadedClassesCount"));
        } else {
            preStatement.setFloat(9, 0.0f);
        }

        if (this.properties.containsKey("usedBufferedMemory")) {
            preStatement.setFloat(10, this.properties.get("usedBufferedMemory"));
        } else {
            preStatement.setFloat(10, 0.0f);
        }

        if (this.properties.containsKey("usedNonHeapMemory")) {
            preStatement.setFloat(11, this.properties.get("usedNonHeapMemory"));
        } else {
            preStatement.setFloat(11, 0.0f);
        }

        if (this.properties.containsKey("usedPhysicalMemorySize")) {
            preStatement.setFloat(12, this.properties.get("usedPhysicalMemorySize"));
        } else {
            preStatement.setFloat(12, 0.0f);
        }

        if (this.properties.containsKey("usedSwapSpaceSize")) {
            preStatement.setFloat(13, this.properties.get("usedSwapSpaceSize"));
        } else {
            preStatement.setFloat(13, 0.0f);
        }

        if (this.properties.containsKey("systemLoad")) {
            preStatement.setFloat(14, this.properties.get("systemLoad"));
        } else {
            preStatement.setFloat(14, 0.0f);
        }

        if (this.properties.containsKey("systemCpuLoad")) {
            preStatement.setFloat(15, this.properties.get("systemCpuLoad"));
        } else {
            preStatement.setFloat(15, 0.0f);
        }

        if (this.properties.containsKey("fileDescriptors")) {
            preStatement.setFloat(16, this.properties.get("fileDescriptors"));
        } else {
            preStatement.setFloat(16, 0.0f);
        }

        if (this.properties.containsKey("httpSessionsMeanAge")) {
            preStatement.setFloat(17, this.properties.get("httpSessionsMeanAge"));
        } else {
            preStatement.setFloat(17, 0.0f);
        }

        if (this.properties.containsKey("transactionsRate")) {
            preStatement.setFloat(18, this.properties.get("transactionsRate"));
        } else {
            preStatement.setFloat(18, 0.0f);
        }

        if (this.properties.containsKey("Free_disk_space")) {
            preStatement.setFloat(19, this.properties.get("Free_disk_space"));
        } else {
            preStatement.setFloat(19, 0.0f);
        }

        if (this.properties.containsKey("Usable_disk_space")) {
            preStatement.setFloat(20, this.properties.get("Usable_disk_space"));
        } else {
            preStatement.setFloat(20, 0.0f);
        }

        if (this.properties.containsKey("tomcatBusyThreads")) {
            preStatement.setFloat(21, this.properties.get("tomcatBusyThreads"));
        } else {
            preStatement.setFloat(21, 0.0f);
        }

        if (this.properties.containsKey("tomcatBytesReceived")) {
            preStatement.setFloat(22, this.properties.get("tomcatBytesReceived"));
        } else {
            preStatement.setFloat(22, 0.0f);
        }

        if (this.properties.containsKey("tomcatBytesSent")) {
            preStatement.setFloat(23, this.properties.get("tomcatBytesSent"));
        } else {
            preStatement.setFloat(23, 0.0f);
        }

        if (this.properties.containsKey("httpHitsRate")) {
            preStatement.setFloat(24, this.properties.get("httpHitsRate"));
        } else {
            preStatement.setFloat(24, 0.0f);
        }

        if (this.properties.containsKey("sqlHitsRate")) {
            preStatement.setFloat(25, this.properties.get("sqlHitsRate"));
        } else {
            preStatement.setFloat(25, 0.0f);
        }
        if (this.properties.containsKey("httpMeanTimes")) {
            preStatement.setFloat(26, this.properties.get("httpMeanTimes"));
        } else {
            preStatement.setFloat(26, 0.0f);
        }
        if (this.properties.containsKey("httpSystemErrors")) {
            preStatement.setFloat(27, this.properties.get("httpSystemErrors"));
        } else {
            preStatement.setFloat(27, 0.0f);
        }
        if (this.properties.containsKey("sqlMeanTimes")) {
            preStatement.setFloat(28, this.properties.get("sqlMeanTimes"));
        } else {
            preStatement.setFloat(28, 0.0f);
        }
        if (this.properties.containsKey("sqlSystemErrors")) {
            preStatement.setFloat(29, this.properties.get("sqlSystemErrors"));
        } else {
            preStatement.setFloat(29, 0.0f);
        }
        preStatement.setTimestamp(30, new Timestamp((new Date()).getTime()));
    }

    @Override
    public void stop() {
    }
}
