-- 大模型管理菜单 SQL

-- 菜单 ID 118 (假设 118 未被占用，如占用请修改)
-- 父菜单 ID 3 (系统工具)

INSERT INTO `sys_menu` VALUES (118, '大模型管理', 3, 4, 'llm', 'tool/llm/index', NULL, '', 1, 0, 'C', '0', '0', 'tool:llm:list', 'druid', 'admin', sysdate(), '', NULL, '大模型管理菜单');

-- 按钮权限 (可选)
-- 查询
INSERT INTO `sys_menu` VALUES (1180, '大模型查询', 118, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'tool:llm:query', '#', 'admin', sysdate(), '', NULL, '');
-- 新增
INSERT INTO `sys_menu` VALUES (1181, '大模型新增', 118, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'tool:llm:add', '#', 'admin', sysdate(), '', NULL, '');
-- 修改
INSERT INTO `sys_menu` VALUES (1182, '大模型修改', 118, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'tool:llm:edit', '#', 'admin', sysdate(), '', NULL, '');
-- 删除
INSERT INTO `sys_menu` VALUES (1183, '大模型删除', 118, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'tool:llm:remove', '#', 'admin', sysdate(), '', NULL, '');
