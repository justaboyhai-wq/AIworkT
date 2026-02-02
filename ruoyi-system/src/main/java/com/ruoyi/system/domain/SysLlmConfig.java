package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 大模型配置对象 sys_llm_config
 * 
 * @author ruoyi
 */
public class SysLlmConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 配置名称 */
    private String name;

    /** 提供商 (OpenAI, DeepSeek, AliQwen, etc.) */
    private String provider;

    /** 模型编码 (gpt-4, deepseek-chat, qwen-max) */
    private String modelCode;

    /** API Key */
    private String apiKey;

    /** Base URL (e.g. https://api.openai.com/v1) */
    private String baseUrl;

    /** 状态 (0正常 1停用) */
    private String status;

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
    public void setProvider(String provider) 
    {
        this.provider = provider;
    }

    public String getProvider() 
    {
        return provider;
    }
    public void setModelCode(String modelCode) 
    {
        this.modelCode = modelCode;
    }

    public String getModelCode() 
    {
        return modelCode;
    }
    public void setApiKey(String apiKey) 
    {
        this.apiKey = apiKey;
    }

    public String getApiKey() 
    {
        return apiKey;
    }
    public void setBaseUrl(String baseUrl) 
    {
        this.baseUrl = baseUrl;
    }

    public String getBaseUrl() 
    {
        return baseUrl;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("provider", getProvider())
            .append("modelCode", getModelCode())
            .append("apiKey", getApiKey())
            .append("baseUrl", getBaseUrl())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
