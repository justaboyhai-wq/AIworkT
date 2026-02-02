package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysDataSource;

/**
 * 数据源配置Service接口
 * 
 * @author ruoyi
 */
public interface ISysDataSourceService 
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
     * 批量删除数据源配置
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysDataSourceByIds(Long[] ids);

    /**
     * 删除数据源配置信息
     * 
     * @param id 数据源配置ID
     * @return 结果
     */
    public int deleteSysDataSourceById(Long id);

    /**
     * 测试数据源连接
     * 
     * @param sysDataSource 数据源配置
     * @return 结果
     */
    public String testConnection(SysDataSource sysDataSource);

    /**
     * 测试数据源连接并返回详细信息（包括耗时）
     * 
     * @param sysDataSource 数据源配置
     * @return 包含status(true/false)和msg/latency的Map
      */
     public java.util.Map<String, Object> testConnectionWithInfo(SysDataSource sysDataSource);

    /**
     * 获取数据源元数据（表和列）
     * 
     * @param id 数据源ID
     * @return 元数据列表
     */
    public java.util.List<java.util.Map<String, Object>> getDataSourceMetadata(Long id);
 }
