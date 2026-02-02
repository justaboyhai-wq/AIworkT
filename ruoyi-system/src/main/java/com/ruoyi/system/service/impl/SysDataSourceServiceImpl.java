package com.ruoyi.system.service.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import javax.annotation.PostConstruct;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysDataSourceMapper;
import com.ruoyi.system.domain.SysDataSource;
import com.ruoyi.system.service.ISysDataSourceService;
import com.alibaba.druid.pool.DruidDataSource;

/**
 * 数据源配置Service业务层处理
 * 
 * @author ruoyi
 */
@Service
public class SysDataSourceServiceImpl implements ISysDataSourceService 
{
    @Autowired
    private SysDataSourceMapper sysDataSourceMapper;

    @PostConstruct
    public void init() {
        try {
            sysDataSourceMapper.createTable();
        } catch (Exception e) {
            System.out.println("Init table sys_datasource failed or already exists: " + e.getMessage());
        }
    }

    /**
     * 查询数据源配置
     * 
     * @param id 数据源配置ID
     * @return 数据源配置
     */
    @Override
    public SysDataSource selectSysDataSourceById(Long id)
    {
        return sysDataSourceMapper.selectSysDataSourceById(id);
    }

    /**
     * 查询数据源配置列表
     * 
     * @param sysDataSource 数据源配置
     * @return 数据源配置
     */
    @Override
    public List<SysDataSource> selectSysDataSourceList(SysDataSource sysDataSource)
    {
        return sysDataSourceMapper.selectSysDataSourceList(sysDataSource);
    }

    /**
     * 新增数据源配置
     * 
     * @param sysDataSource 数据源配置
     * @return 结果
     */
    @Override
    public int insertSysDataSource(SysDataSource sysDataSource)
    {
        sysDataSource.setCreateTime(DateUtils.getNowDate());
        return sysDataSourceMapper.insertSysDataSource(sysDataSource);
    }

    /**
     * 修改数据源配置
     * 
     * @param sysDataSource 数据源配置
     * @return 结果
     */
    @Override
    public int updateSysDataSource(SysDataSource sysDataSource)
    {
        sysDataSource.setUpdateTime(DateUtils.getNowDate());
        return sysDataSourceMapper.updateSysDataSource(sysDataSource);
    }

    /**
     * 批量删除数据源配置
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSysDataSourceByIds(Long[] ids)
    {
        return sysDataSourceMapper.deleteSysDataSourceByIds(ids);
    }

    /**
     * 删除数据源配置信息
     * 
     * @param id 数据源配置ID
     * @return 结果
     */
    @Override
    public int deleteSysDataSourceById(Long id)
    {
        return sysDataSourceMapper.deleteSysDataSourceById(id);
    }

    @Override
    public String testConnection(SysDataSource sysDataSource) {
        String url = "";
        String driver = sysDataSource.getDriverClass();
        if (driver == null || driver.isEmpty()) {
            if ("MySQL".equalsIgnoreCase(sysDataSource.getType())) {
                driver = "com.mysql.cj.jdbc.Driver";
                url = "jdbc:mysql://" + sysDataSource.getHost() + ":" + sysDataSource.getPort() + "/" + sysDataSource.getDbName() + "?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8";
            } else if ("PostgreSQL".equalsIgnoreCase(sysDataSource.getType())) {
                driver = "org.postgresql.Driver";
                url = "jdbc:postgresql://" + sysDataSource.getHost() + ":" + sysDataSource.getPort() + "/" + sysDataSource.getDbName();
            } else if ("Oracle".equalsIgnoreCase(sysDataSource.getType())) {
                driver = "oracle.jdbc.driver.OracleDriver";
                url = "jdbc:oracle:thin:@" + sysDataSource.getHost() + ":" + sysDataSource.getPort() + ":" + sysDataSource.getDbName();
            } else if ("SQLServer".equalsIgnoreCase(sysDataSource.getType())) {
                driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
                url = "jdbc:sqlserver://" + sysDataSource.getHost() + ":" + sysDataSource.getPort() + ";DatabaseName=" + sysDataSource.getDbName();
            }
        } else {
            // If driver is provided, construct URL based on type if possible, or assume standard format
            if ("MySQL".equalsIgnoreCase(sysDataSource.getType())) {
                url = "jdbc:mysql://" + sysDataSource.getHost() + ":" + sysDataSource.getPort() + "/" + sysDataSource.getDbName() + "?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8";
            } else if ("PostgreSQL".equalsIgnoreCase(sysDataSource.getType())) {
                url = "jdbc:postgresql://" + sysDataSource.getHost() + ":" + sysDataSource.getPort() + "/" + sysDataSource.getDbName();
            } else if ("Oracle".equalsIgnoreCase(sysDataSource.getType())) {
                url = "jdbc:oracle:thin:@" + sysDataSource.getHost() + ":" + sysDataSource.getPort() + ":" + sysDataSource.getDbName();
            } else if ("SQLServer".equalsIgnoreCase(sysDataSource.getType())) {
                url = "jdbc:sqlserver://" + sysDataSource.getHost() + ":" + sysDataSource.getPort() + ";DatabaseName=" + sysDataSource.getDbName();
            }
        }
        
        DruidDataSource dataSource = null;
        Connection conn = null;
        try {
            // Use Druid to test, to simulate real pool behavior
            dataSource = new DruidDataSource();
            dataSource.setDriverClassName(driver);
            dataSource.setUrl(url);
            dataSource.setUsername(sysDataSource.getUsername());
            dataSource.setPassword(sysDataSource.getPassword());
            dataSource.setInitialSize(1);
            dataSource.setMinIdle(1);
            dataSource.setMaxActive(1);
            dataSource.setMaxWait(3000); // 3s timeout
            
            dataSource.init();
            conn = dataSource.getConnection();
            if (conn != null) {
                return "success";
            }
        } catch (Exception e) {
            String msg = e.getMessage();
            if (msg != null) {
                if (msg.contains("Communications link failure")) {
                    return "连接失败：无法连接到数据库服务器，请检查主机地址和端口是否正确，以及网络是否通畅。";
                } else if (msg.contains("Access denied")) {
                    return "连接失败：用户名或密码错误。";
                } else if (msg.contains("Unknown database")) {
                    return "连接失败：数据库不存在。";
                }
            }
            return "连接失败: " + msg;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (dataSource != null) {
                dataSource.close();
            }
        }
        return "failed";
    }

    @Override
    public java.util.Map<String, Object> testConnectionWithInfo(SysDataSource sysDataSource) {
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        long startTime = System.currentTimeMillis();
        String testResult = testConnection(sysDataSource);
        long endTime = System.currentTimeMillis();
        long latency = endTime - startTime;

        if ("success".equals(testResult)) {
            result.put("status", true);
            result.put("latency", latency);
            result.put("activeConn", 0); // Currently 0 as we don't maintain persistent pool
            result.put("maxConn", sysDataSource.getMaxActive());
        } else {
            result.put("status", false);
            result.put("msg", testResult);
        }
        return result;
    }

    @Override
    public List<java.util.Map<String, Object>> getDataSourceMetadata(Long id) {
        SysDataSource sysDataSource = selectSysDataSourceById(id);
        if (sysDataSource == null) {
            throw new RuntimeException("Data source not found");
        }

        List<java.util.Map<String, Object>> tables = new java.util.ArrayList<>();
        String url = "";
        String driver = sysDataSource.getDriverClass();
        
        // Logic similar to testConnection to determine driver/url if missing
        if (driver == null || driver.isEmpty()) {
             if ("MySQL".equalsIgnoreCase(sysDataSource.getType())) {
                driver = "com.mysql.cj.jdbc.Driver";
                url = "jdbc:mysql://" + sysDataSource.getHost() + ":" + sysDataSource.getPort() + "/" + sysDataSource.getDbName() + "?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8";
            } else if ("PostgreSQL".equalsIgnoreCase(sysDataSource.getType())) {
                driver = "org.postgresql.Driver";
                url = "jdbc:postgresql://" + sysDataSource.getHost() + ":" + sysDataSource.getPort() + "/" + sysDataSource.getDbName();
            } else if ("Oracle".equalsIgnoreCase(sysDataSource.getType())) {
                driver = "oracle.jdbc.driver.OracleDriver";
                url = "jdbc:oracle:thin:@" + sysDataSource.getHost() + ":" + sysDataSource.getPort() + ":" + sysDataSource.getDbName();
            } else if ("SQLServer".equalsIgnoreCase(sysDataSource.getType())) {
                driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
                url = "jdbc:sqlserver://" + sysDataSource.getHost() + ":" + sysDataSource.getPort() + ";DatabaseName=" + sysDataSource.getDbName();
            }
        } else {
            // Standard URL construction if driver present but URL logic reused
             if ("MySQL".equalsIgnoreCase(sysDataSource.getType())) {
                url = "jdbc:mysql://" + sysDataSource.getHost() + ":" + sysDataSource.getPort() + "/" + sysDataSource.getDbName() + "?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8";
            } else if ("PostgreSQL".equalsIgnoreCase(sysDataSource.getType())) {
                url = "jdbc:postgresql://" + sysDataSource.getHost() + ":" + sysDataSource.getPort() + "/" + sysDataSource.getDbName();
            } else if ("Oracle".equalsIgnoreCase(sysDataSource.getType())) {
                url = "jdbc:oracle:thin:@" + sysDataSource.getHost() + ":" + sysDataSource.getPort() + ":" + sysDataSource.getDbName();
            } else if ("SQLServer".equalsIgnoreCase(sysDataSource.getType())) {
                url = "jdbc:sqlserver://" + sysDataSource.getHost() + ":" + sysDataSource.getPort() + ";DatabaseName=" + sysDataSource.getDbName();
            }
        }

        Connection conn = null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, sysDataSource.getUsername(), sysDataSource.getPassword());
            java.sql.DatabaseMetaData meta = conn.getMetaData();
            
            // Get Tables
            String catalog = null;
            String schema = null;
            if ("Oracle".equalsIgnoreCase(sysDataSource.getType()) || "PostgreSQL".equalsIgnoreCase(sysDataSource.getType())) {
                schema = sysDataSource.getUsername().toUpperCase(); // Often schema is username
            }
            if ("MySQL".equalsIgnoreCase(sysDataSource.getType())) {
                catalog = sysDataSource.getDbName();
            }
            
            // Adjust for PostgreSQL schema if needed, but default to null to search all or rely on connection
            if ("PostgreSQL".equalsIgnoreCase(sysDataSource.getType())) {
                schema = "public"; // Default public schema
            }
            
            java.sql.ResultSet rs = meta.getTables(catalog, schema, "%", new String[]{"TABLE"});
            while (rs.next()) {
                java.util.Map<String, Object> table = new java.util.HashMap<>();
                String tableName = rs.getString("TABLE_NAME");
                table.put("name", tableName);
                table.put("comment", rs.getString("REMARKS"));
                
                // Get Columns for this table
                List<java.util.Map<String, Object>> columns = new java.util.ArrayList<>();
                java.sql.ResultSet cols = meta.getColumns(catalog, schema, tableName, "%");
                while (cols.next()) {
                    java.util.Map<String, Object> col = new java.util.HashMap<>();
                    col.put("name", cols.getString("COLUMN_NAME"));
                    col.put("type", cols.getString("TYPE_NAME"));
                    col.put("comment", cols.getString("REMARKS"));
                    columns.add(col);
                }
                cols.close();
                table.put("columns", columns);

                // Get Imported Keys (Foreign Keys)
                List<java.util.Map<String, Object>> importedKeys = new java.util.ArrayList<>();
                try {
                    java.sql.ResultSet fks = meta.getImportedKeys(catalog, schema, tableName);
                    while (fks.next()) {
                        java.util.Map<String, Object> fk = new java.util.HashMap<>();
                        fk.put("pktable", fks.getString("PKTABLE_NAME")); // Referenced table
                        fk.put("pkcolumn", fks.getString("PKCOLUMN_NAME")); // Referenced column
                        fk.put("fktable", fks.getString("FKTABLE_NAME")); // Current table (should match tableName)
                        fk.put("fkcolumn", fks.getString("FKCOLUMN_NAME")); // Current table column
                        importedKeys.add(fk);
                    }
                    fks.close();
                } catch (Exception e) {
                    // Ignore error if foreign keys cannot be fetched
                    System.out.println("Could not fetch imported keys for table " + tableName + ": " + e.getMessage());
                }
                table.put("importedKeys", importedKeys);
                
                tables.add(table);
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch metadata: " + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return tables;
    }
}
