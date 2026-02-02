-- 菜单 SQL
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('118', '数据管理', '3', '4', 'datasource', 'tool/datasource/index', 0, 0, 'C', '0', '0', 'tool:datasource:list', 'server', 'admin', sysdate(), '', null, '数据源管理菜单');

-- 按钮 SQL
insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('1061', '数据源查询', '118', '1', '#', '', 1, 0, 'F', '0', '0', 'tool:datasource:query', '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('1062', '数据源新增', '118', '2', '#', '', 1, 0, 'F', '0', '0', 'tool:datasource:add', '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('1063', '数据源修改', '118', '3', '#', '', 1, 0, 'F', '0', '0', 'tool:datasource:edit', '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('1064', '数据源删除', '118', '4', '#', '', 1, 0, 'F', '0', '0', 'tool:datasource:remove', '#', 'admin', sysdate(), '', null, '');

-- 角色菜单关联 (自动授权给 admin 角色 id=2? No, admin usually has all access. But let's add to role 2 just in case if that's the common pattern, though usually admin is superuser. Role 2 is usually "common user" or "tester". Let's skip role-menu insert for now to avoid conflicts, admin usually bypasses checks or has all permissions).
