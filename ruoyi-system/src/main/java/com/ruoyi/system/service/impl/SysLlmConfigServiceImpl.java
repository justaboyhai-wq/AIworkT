package com.ruoyi.system.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.annotation.PostConstruct;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysLlmConfigMapper;
import com.ruoyi.system.domain.SysLlmConfig;
import com.ruoyi.system.service.ISysLlmConfigService;

/**
 * 大模型配置Service业务层处理
 * 
 * @author ruoyi
 */
@Service
public class SysLlmConfigServiceImpl implements ISysLlmConfigService 
{
    @Autowired
    private SysLlmConfigMapper sysLlmConfigMapper;

    @PostConstruct
    public void init() {
        try {
            sysLlmConfigMapper.createTable();
        } catch (Exception e) {
            System.out.println("Init table sys_llm_config failed or already exists: " + e.getMessage());
        }
    }

    /**
     * 查询大模型配置
     * 
     * @param id 大模型配置ID
     * @return 大模型配置
     */
    @Override
    public SysLlmConfig selectSysLlmConfigById(Long id)
    {
        return sysLlmConfigMapper.selectSysLlmConfigById(id);
    }

    /**
     * 查询大模型配置列表
     * 
     * @param sysLlmConfig 大模型配置
     * @return 大模型配置
     */
    @Override
    public List<SysLlmConfig> selectSysLlmConfigList(SysLlmConfig sysLlmConfig)
    {
        return sysLlmConfigMapper.selectSysLlmConfigList(sysLlmConfig);
    }

    /**
     * 新增大模型配置
     * 
     * @param sysLlmConfig 大模型配置
     * @return 结果
     */
    @Override
    public int insertSysLlmConfig(SysLlmConfig sysLlmConfig)
    {
        sysLlmConfig.setCreateTime(DateUtils.getNowDate());
        return sysLlmConfigMapper.insertSysLlmConfig(sysLlmConfig);
    }

    /**
     * 修改大模型配置
     * 
     * @param sysLlmConfig 大模型配置
     * @return 结果
     */
    @Override
    public int updateSysLlmConfig(SysLlmConfig sysLlmConfig)
    {
        sysLlmConfig.setUpdateTime(DateUtils.getNowDate());
        return sysLlmConfigMapper.updateSysLlmConfig(sysLlmConfig);
    }

    /**
     * 批量删除大模型配置
     * 
     * @param ids 需要删除的大模型配置ID
     * @return 结果
     */
    @Override
    public int deleteSysLlmConfigByIds(Long[] ids)
    {
        return sysLlmConfigMapper.deleteSysLlmConfigByIds(ids);
    }

    /**
     * 删除大模型配置信息
     * 
     * @param id 大模型配置ID
     * @return 结果
     */
    @Override
    public int deleteSysLlmConfigById(Long id)
    {
        return sysLlmConfigMapper.deleteSysLlmConfigById(id);
    }

    @Override
    public String testConnection(SysLlmConfig config) {
        String baseUrl = config.getBaseUrl();
        String apiKey = config.getApiKey();
        String model = config.getModelCode();

        if (baseUrl == null || baseUrl.isEmpty()) {
            return "Base URL不能为空";
        }
        // Normalize URL: remove trailing slash
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        
        // Simple Chat Completion Request
        // Most providers (OpenAI, DeepSeek, Qwen-OpenAI-Compatible) support /v1/chat/completions
        String endpoint = baseUrl + "/chat/completions";
        // If user already included /v1 or /chat/completions in base url, handle it? 
        // Let's assume user provides host like https://api.openai.com/v1
        
        if (!baseUrl.contains("/v1") && !baseUrl.contains("azure")) {
             // Heuristic: Append /v1 if missing and not azure
             endpoint = baseUrl + "/v1/chat/completions";
        } else {
             endpoint = baseUrl + "/chat/completions";
        }

        try {
            URL url = new URL(endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + apiKey);
            conn.setDoOutput(true);
            conn.setConnectTimeout(5000); // 5s timeout
            conn.setReadTimeout(10000);

            // Minimal payload
            String payload = "{"
                    + "\"model\": \"" + model + "\","
                    + "\"messages\": [{\"role\": \"user\", \"content\": \"Hi\"}],"
                    + "\"max_tokens\": 5"
                    + "}";

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = payload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int code = conn.getResponseCode();
            if (code == 200) {
                return null; // Success
            } else {
                StringBuilder errorResponse = new StringBuilder();
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        errorResponse.append(line);
                    }
                }
                return "HTTP " + code + ": " + errorResponse.toString();
            }
        } catch (Exception e) {
            return "Connection failed: " + e.getMessage();
        }
    }
}
