package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysLlmConfig;

/**
 * 大模型配置Service接口
 * 
 * @author ruoyi
 */
public interface ISysLlmConfigService 
{
    /**
     * 查询大模型配置
     * 
     * @param id 大模型配置ID
     * @return 大模型配置
     */
    public SysLlmConfig selectSysLlmConfigById(Long id);

    /**
     * 查询大模型配置列表
     * 
     * @param sysLlmConfig 大模型配置
     * @return 大模型配置集合
     */
    public List<SysLlmConfig> selectSysLlmConfigList(SysLlmConfig sysLlmConfig);

    /**
     * 新增大模型配置
     * 
     * @param sysLlmConfig 大模型配置
     * @return 结果
     */
    public int insertSysLlmConfig(SysLlmConfig sysLlmConfig);

    /**
     * 修改大模型配置
     * 
     * @param sysLlmConfig 大模型配置
     * @return 结果
     */
    public int updateSysLlmConfig(SysLlmConfig sysLlmConfig);

    /**
     * 批量删除大模型配置
     * 
     * @param ids 需要删除的大模型配置ID
     * @return 结果
     */
    public int deleteSysLlmConfigByIds(Long[] ids);

    /**
     * 删除大模型配置信息
     * 
     * @param id 大模型配置ID
     * @return 结果
     */
    public int deleteSysLlmConfigById(Long id);

    /**
     * 测试连接
     * 
     * @param sysLlmConfig 配置信息
     * @return 测试结果 (null or empty string if success, error message otherwise)
     */
    public String testConnection(SysLlmConfig sysLlmConfig);
}
