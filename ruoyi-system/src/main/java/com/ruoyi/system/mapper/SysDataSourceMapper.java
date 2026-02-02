package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SysDataSource;
import org.apache.ibatis.annotations.Update;

/**
 * 数据源配置Mapper接口
 * 
 * @author ruoyi
 */
public interface SysDataSourceMapper 
{
    /**
     * 查询数据源配置
     * 
     * @param id 数据源配置ID
     * @return 数据源配置
     */
    public SysDataSource selectSysDataSourceById(Long id);

    /**
     * 查询数据源配置列表
     * 
     * @param sysDataSource 数据源配置
     * @return 数据源配置集合
     */
    public List<SysDataSource> selectSysDataSourceList(SysDataSource sysDataSource);

    /**
     * 新增数据源配置
     * 
     * @param sysDataSource 数据源配置
     * @return 结果
     */
    public int insertSysDataSource(SysDataSource sysDataSource);

    /**
     * 修改数据源配置
     * 
     * @param sysDataSource 数据源配置
     * @return 结果
     */
    public int updateSysDataSource(SysDataSource sysDataSource);

    /**
     * 删除数据源配置
     * 
     * @param id 数据源配置ID
     * @return 结果
     */
    public int deleteSysDataSourceById(Long id);

    /**
     * 批量删除数据源配置
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysDataSourceByIds(Long[] ids);

    @Update("CREATE TABLE IF NOT EXISTS `sys_datasource` (\n" +
            "  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',\n" +
            "  `name` varchar(100) DEFAULT NULL COMMENT '连接名称',\n" +
            "  `type` varchar(50) DEFAULT NULL COMMENT '数据库类型',\n" +
            "  `driver_class` varchar(100) DEFAULT NULL COMMENT '驱动类',\n" +
            "  `host` varchar(255) DEFAULT NULL COMMENT '主机',\n" +
            "  `port` varchar(10) DEFAULT NULL COMMENT '端口',\n" +
            "  `username` varchar(100) DEFAULT NULL COMMENT '用户名',\n" +
            "  `password` varchar(100) DEFAULT NULL COMMENT '密码',\n" +
            "  `db_name` varchar(100) DEFAULT NULL COMMENT '数据库名',\n" +
            "  `initial_size` int(11) DEFAULT '5' COMMENT '初始连接',\n" +
            "  `max_active` int(11) DEFAULT '20' COMMENT '最大连接',\n" +
            "  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',\n" +
            "  `create_time` datetime DEFAULT NULL COMMENT '创建时间',\n" +
            "  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',\n" +
            "  `update_time` datetime DEFAULT NULL COMMENT '更新时间',\n" +
            "  `remark` varchar(500) DEFAULT NULL COMMENT '备注',\n" +
            "  PRIMARY KEY (`id`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据源配置表';")
    public void createTable();
}
