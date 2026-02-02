package com.ruoyi.web.controller.tool;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.SysLlmConfig;
import com.ruoyi.system.service.ISysLlmConfigService;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 大模型配置Controller
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/tool/llm")
public class SysLlmConfigController extends BaseController
{
    @Autowired
    private ISysLlmConfigService sysLlmConfigService;

    /**
     * 查询大模型配置列表
     */
    @PreAuthorize("@ss.hasPermi('tool:llm:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysLlmConfig sysLlmConfig)
    {
        startPage();
        List<SysLlmConfig> list = sysLlmConfigService.selectSysLlmConfigList(sysLlmConfig);
        return getDataTable(list);
    }

    /**
     * 获取大模型配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('tool:llm:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sysLlmConfigService.selectSysLlmConfigById(id));
    }

    /**
     * 新增大模型配置
     */
    @PreAuthorize("@ss.hasPermi('tool:llm:add')")
    @Log(title = "大模型配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysLlmConfig sysLlmConfig)
    {
        return toAjax(sysLlmConfigService.insertSysLlmConfig(sysLlmConfig));
    }

    /**
     * 修改大模型配置
     */
    @PreAuthorize("@ss.hasPermi('tool:llm:edit')")
    @Log(title = "大模型配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysLlmConfig sysLlmConfig)
    {
        return toAjax(sysLlmConfigService.updateSysLlmConfig(sysLlmConfig));
    }

    /**
     * 删除大模型配置
     */
    @PreAuthorize("@ss.hasPermi('tool:llm:remove')")
    @Log(title = "大模型配置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sysLlmConfigService.deleteSysLlmConfigByIds(ids));
    }

    /**
     * 测试连接
     */
    @PreAuthorize("@ss.hasPermi('tool:llm:query')")
    @PostMapping("/test")
    public AjaxResult testConnection(@RequestBody SysLlmConfig sysLlmConfig)
    {
        String result = sysLlmConfigService.testConnection(sysLlmConfig);
        if (result == null || result.isEmpty()) {
            return AjaxResult.success("连接成功");
        } else {
            return AjaxResult.error("连接失败: " + result);
        }
    }
}
