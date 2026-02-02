package com.ruoyi.web.controller.tool;

import com.ruoyi.common.core.domain.entity.SysMenu;
import com.ruoyi.system.mapper.SysMenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 初始化大模型管理菜单
 */
@Component
public class SysLlmMenuRunner implements CommandLineRunner {

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Override
    public void run(String... args) throws Exception {
        // 1. Restore Data Source Menu (ID 118)
        Long dsMenuId = 118L;
        SysMenu dsMenu = sysMenuMapper.selectMenuById(dsMenuId);
        if (dsMenu == null) {
            dsMenu = new SysMenu();
            dsMenu.setMenuId(dsMenuId);
            initDataSourceMenu(dsMenu);
            sysMenuMapper.insertMenu(dsMenu);
            System.out.println("========== Restored Data Source Menu (ID 118) ==========");
        } else {
            // Force update to ensure it's Data Source
            initDataSourceMenu(dsMenu);
            sysMenuMapper.updateMenu(dsMenu);
            System.out.println("========== Updated/Fixed Data Source Menu (ID 118) ==========");
        }

        // 2. Create/Update LLM Menu (ID 120)
        Long llmMenuId = 120L;
        SysMenu llmMenu = sysMenuMapper.selectMenuById(llmMenuId);
        if (llmMenu == null) {
            llmMenu = new SysMenu();
            llmMenu.setMenuId(llmMenuId);
            initLlmMenu(llmMenu);
            sysMenuMapper.insertMenu(llmMenu);
            System.out.println("========== Created LLM Menu (ID 120) ==========");
        } else {
            initLlmMenu(llmMenu);
            sysMenuMapper.updateMenu(llmMenu);
            System.out.println("========== Updated LLM Menu (ID 120) ==========");
        }
    }

    private void initDataSourceMenu(SysMenu menu) {
        menu.setMenuName("数据管理");
        menu.setParentId(3L);
        menu.setOrderNum(4);
        menu.setPath("datasource");
        menu.setComponent("tool/datasource/index");
        menu.setIsFrame("1");
        menu.setIsCache("0");
        menu.setMenuType("C");
        menu.setVisible("0");
        menu.setStatus("0");
        menu.setPerms("tool:datasource:list");
        menu.setIcon("server");
        menu.setRemark("数据源管理菜单");
        if (menu.getCreateBy() == null) menu.setCreateBy("admin");
    }

    private void initLlmMenu(SysMenu menu) {
        menu.setMenuName("大模型管理");
        menu.setParentId(3L);
        menu.setOrderNum(5); // Order 5, after Data Source
        menu.setPath("llm");
        menu.setComponent("tool/llm/index");
        menu.setIsFrame("1");
        menu.setIsCache("0");
        menu.setMenuType("C");
        menu.setVisible("0");
        menu.setStatus("0");
        menu.setPerms("tool:llm:list");
        menu.setIcon("druid");
        menu.setRemark("大模型管理菜单");
        if (menu.getCreateBy() == null) menu.setCreateBy("admin");
    }
}
