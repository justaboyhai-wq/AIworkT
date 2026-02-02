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
import com.ruoyi.system.domain.SysDataSource;
import com.ruoyi.system.service.ISysDataSourceService;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 数据源配置Controller
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/tool/datasource")
public class SysDataSourceController extends BaseController
{
    @Autowired
    private ISysDataSourceService sysDataSourceService;

    /**
     * 查询数据源配置列表
     */
    @PreAuthorize("@ss.hasPermi('tool:datasource:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysDataSource sysDataSource)
    {
        startPage();
        List<SysDataSource> list = sysDataSourceService.selectSysDataSourceList(sysDataSource);
        return getDataTable(list);
    }

    /**
     * 获取数据源配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('tool:datasource:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sysDataSourceService.selectSysDataSourceById(id));
    }

    /**
     * 新增数据源配置
     */
    @PreAuthorize("@ss.hasPermi('tool:datasource:add')")
    @Log(title = "数据源配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysDataSource sysDataSource)
    {
        return toAjax(sysDataSourceService.insertSysDataSource(sysDataSource));
    }

    /**
     * 修改数据源配置
     */
    @PreAuthorize("@ss.hasPermi('tool:datasource:edit')")
    @Log(title = "数据源配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysDataSource sysDataSource)
    {
        return toAjax(sysDataSourceService.updateSysDataSource(sysDataSource));
    }

    /**
     * 删除数据源配置
     */
    @PreAuthorize("@ss.hasPermi('tool:datasource:remove')")
    @Log(title = "数据源配置", businessType = BusinessType.DELETE)
	@DeleteMapping(value = "/{ids}")
    public AjaxResult remove(@PathVariable("ids") Long[] ids)
    {
        return toAjax(sysDataSourceService.deleteSysDataSourceByIds(ids));
    }

    /**
     * 测试连接
     */
    @PreAuthorize("@ss.hasPermi('tool:datasource:query')")
    @PostMapping("/test")
    public AjaxResult testConnection(@RequestBody SysDataSource sysDataSource)
    {
        java.util.Map<String, Object> result = sysDataSourceService.testConnectionWithInfo(sysDataSource);
        if ((Boolean) result.get("status")) {
            return AjaxResult.success("连接成功", result);
        } else {
            return AjaxResult.error((String) result.get("msg"));
        }
    }

    /**
     * 获取数据源元数据（Schema）
     */
    @PreAuthorize("@ss.hasPermi('tool:datasource:query')")
    @GetMapping("/{id}/schema")
    public AjaxResult getSchema(@PathVariable("id") Long id)
    {
        try {
            return AjaxResult.success(sysDataSourceService.getDataSourceMetadata(id));
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }
}
