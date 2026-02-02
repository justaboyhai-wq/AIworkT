package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 数据源配置对象 sys_datasource
 * 
 * @author ruoyi
 */
public class SysDataSource extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 连接名称 */
    private String name;

    /** 数据库类型 */
    private String type;

    /** 驱动类 */
    private String driverClass;

    /** 主机 */
    private String host;

    /** 端口 */
    private String port;

    /** 用户名 */
    private String username;

    /** 密码 */
    private String password;

    /** 数据库名 */
    private String dbName;

    /** 初始连接 */
    private Integer initialSize;

    /** 最大连接 */
    private Integer maxActive;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setType(String type) 
    {
        this.type = type;
    }

    public String getType() 
    {
        return type;
    }
    public void setDriverClass(String driverClass) 
    {
        this.driverClass = driverClass;
    }

    public String getDriverClass() 
    {
        return driverClass;
    }
    public void setHost(String host) 
    {
        this.host = host;
    }

    public String getHost() 
    {
        return host;
    }
    public void setPort(String port) 
    {
        this.port = port;
    }

    public String getPort() 
    {
        return port;
    }
    public void setUsername(String username) 
    {
        this.username = username;
    }

    public String getUsername() 
    {
        return username;
    }
    public void setPassword(String password) 
    {
        this.password = password;
    }

    public String getPassword() 
    {
        return password;
    }
    public void setDbName(String dbName) 
    {
        this.dbName = dbName;
    }

    public String getDbName() 
    {
        return dbName;
    }
    public void setInitialSize(Integer initialSize) 
    {
        this.initialSize = initialSize;
    }

    public Integer getInitialSize() 
    {
        return initialSize;
    }
    public void setMaxActive(Integer maxActive) 
    {
        this.maxActive = maxActive;
    }

    public Integer getMaxActive() 
    {
        return maxActive;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("type", getType())
            .append("driverClass", getDriverClass())
            .append("host", getHost())
            .append("port", getPort())
            .append("username", getUsername())
            .append("password", getPassword())
            .append("dbName", getDbName())
            .append("initialSize", getInitialSize())
            .append("maxActive", getMaxActive())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
