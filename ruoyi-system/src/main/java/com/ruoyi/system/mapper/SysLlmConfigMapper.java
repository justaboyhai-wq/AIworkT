package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SysLlmConfig;
import org.apache.ibatis.annotations.Update;

/**
 * 大模型配置Mapper接口
 * 
 * @author ruoyi
 */
public interface SysLlmConfigMapper 
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
     * 删除大模型配置
     * 
     * @param id 大模型配置ID
     * @return 结果
     */
    public int deleteSysLlmConfigById(Long id);

    /**
     * 批量删除大模型配置
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysLlmConfigByIds(Long[] ids);

    @Update("CREATE TABLE IF NOT EXISTS sys_llm_config (" +
            "id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID', " +
            "name VARCHAR(100) DEFAULT '' COMMENT '配置名称', " +
            "provider VARCHAR(50) DEFAULT '' COMMENT '提供商', " +
            "model_code VARCHAR(100) DEFAULT '' COMMENT '模型编码', " +
            "api_key VARCHAR(500) DEFAULT '' COMMENT 'API Key', " +
            "base_url VARCHAR(255) DEFAULT '' COMMENT 'Base URL', " +
            "status CHAR(1) DEFAULT '0' COMMENT '状态', " +
            "create_by VARCHAR(64) DEFAULT '' COMMENT '创建者', " +
            "create_time DATETIME COMMENT '创建时间', " +
            "update_by VARCHAR(64) DEFAULT '' COMMENT '更新者', " +
            "update_time DATETIME COMMENT '更新时间', " +
            "remark VARCHAR(500) DEFAULT '' COMMENT '备注', " +
            "PRIMARY KEY (id)) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='大模型配置表'")
    public void createTable();
}
