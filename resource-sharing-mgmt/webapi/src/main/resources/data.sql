-- 初始化数据库库表数据
--1. 管理用户初始化一个admin用户
INSERT INTO d1_web_admin_user (id, create_by_id, update_by_id, account, email, enable, name, password, phone, pinyin,
                               sex, organization_name,role_name)
SELECT 'admin',
       'admin',
       'admin',
       'admin',
       '',
       true,
       'admin',
       'e10adc3949ba59abbe56e057f20f883e',
       '',
       'admin',
       1,
       '总部',
       '系统管理员'
WHERE NOT EXISTS(
        SELECT *
        FROM d1_web_admin_user
        WHERE ID = 'admin'
    );

INSERT INTO d1_role (id, create_by_id, update_by_id, name, remark)
SELECT 'admin',
       'admin',
       'admin',
       '系统管理员',
       ''
WHERE NOT EXISTS(
        SELECT *
        FROM d1_role
        WHERE ID = 'admin'
    );

INSERT INTO d1_role (id, create_by_id, update_by_id, name, remark)
SELECT 'default',
       'default',
       'default',
       '默认角色',
       ''
WHERE NOT EXISTS(
        SELECT *
        FROM d1_role
        WHERE ID = 'default'
    );

INSERT INTO d1_organization (id, create_by_id, update_by_id, id_link,name_link, level, name, parent_id, seq, level_msg,
                             parent_name)
SELECT 'headquarters',
       'headquarters',
       'headquarters',
       'headquarters',
       '总部',
       0,
       '总部',
       '',
       0,
       '一级',
       ''
WHERE NOT EXISTS(
        SELECT *
        FROM d1_organization
        WHERE ID = 'headquarters'
    );

INSERT INTO d1_organization_user (id, create_by_id, organization_id, user_id)
SELECT 'admin',
       'admin',
       'headquarters',
       'admin'

WHERE NOT EXISTS(
        SELECT *
        FROM d1_organization_user
        WHERE ID = 'admin'
    );


INSERT INTO d1_role_user (id, create_by_id, organization_name, role_id, user_account, user_id, user_name, user_phone,
                          role_name)
SELECT 'admin',
       'admin',
       '总部',
       'admin',
       'admin',
       'admin',
       'admin',
       'admin',
       '系统管理员'
WHERE NOT EXISTS(SELECT * FROM d1_role_user WHERE ID = 'admin');



INSERT INTO d1_menu_tree (id, create_by_id, create_time, update_by_id, update_time, icon, id_link, level, level_msg, name, parent_id, parent_name, path, seq,type,has_system) SELECT  '02d0aa6c0e644e3f90aea6cd7cdd3d59', 'admin', '2021-03-19 18:17:33.477', null, null, '', '96050fae0d674f3998ee81a3556b3164_02d0aa6c0e644e3f90aea6cd7cdd3d59', '1', '二级', '数据备份', '96050fae0d674f3998ee81a3556b3164', '系统设置', '/dataBackup', '1', '0', '1' WHERE NOT EXISTS(SELECT * FROM d1_menu_tree WHERE ID = '02d0aa6c0e644e3f90aea6cd7cdd3d59');
INSERT INTO d1_menu_tree (id, create_by_id, create_time, update_by_id, update_time, icon, id_link, level, level_msg, name, parent_id, parent_name, path, seq,type,has_system) SELECT '045bc95a27624d44b94575b610749269', 'admin', '2021-03-19 18:14:25.053', null, null, '', 'ffbfa1599ff34cb8bbdbcdec6fe46b61_045bc95a27624d44b94575b610749269', '1', '二级', '用户日志', 'ffbfa1599ff34cb8bbdbcdec6fe46b61', '日志管理', '/userLogs', '1', '0', '1' WHERE NOT EXISTS(SELECT * FROM d1_menu_tree WHERE ID = '045bc95a27624d44b94575b610749269');
INSERT INTO d1_menu_tree (id, create_by_id, create_time, update_by_id, update_time, icon, id_link, level, level_msg, name, parent_id, parent_name, path, seq,type,has_system) SELECT '1046cf27f46549609da95cc719277eb1', 'admin', '2021-03-19 15:01:47.453', null, null, '', 'b6b8d3c1a18c4165b5070fdf91684815_f7facbd19fcb4ae79c021f1355d03cb9_1046cf27f46549609da95cc719277eb1', '2', '三级', '用户导入按钮', 'f7facbd19fcb4ae79c021f1355d03cb9', '用户管理', 'user_userImport', '7', '2', '1' WHERE NOT EXISTS(SELECT * FROM d1_menu_tree WHERE ID = '1046cf27f46549609da95cc719277eb1');
INSERT INTO d1_menu_tree (id, create_by_id, create_time, update_by_id, update_time, icon, id_link, level, level_msg, name, parent_id, parent_name, path, seq,type,has_system) SELECT '142f0f4993dc4e9d97bbc249e3091926', 'admin', '2021-03-19 14:43:35.907', null, null, '', 'b6b8d3c1a18c4165b5070fdf91684815_dbabc974a6344a90b6da0c7f88c9e424_142f0f4993dc4e9d97bbc249e3091926', '2', '三级', '组织机构编辑按钮', 'dbabc974a6344a90b6da0c7f88c9e424', '组织机构', 'organization_edit', '0', '2', '1' WHERE NOT EXISTS(SELECT * FROM d1_menu_tree WHERE ID = '142f0f4993dc4e9d97bbc249e3091926');
INSERT INTO d1_menu_tree (id, create_by_id, create_time, update_by_id, update_time, icon, id_link, level, level_msg, name, parent_id, parent_name, path, seq,type,has_system) SELECT '180889784e7041e88fc1983571ff2dfb', 'admin', '2021-03-19 15:05:12.549', null, null, '', 'b6b8d3c1a18c4165b5070fdf91684815_f7facbd19fcb4ae79c021f1355d03cb9_180889784e7041e88fc1983571ff2dfb', '2', '三级', '启用按钮', 'f7facbd19fcb4ae79c021f1355d03cb9', '用户管理', 'user_enable', '10', '2', '1' WHERE NOT EXISTS(SELECT * FROM d1_menu_tree WHERE ID = '180889784e7041e88fc1983571ff2dfb');
INSERT INTO d1_menu_tree (id, create_by_id, create_time, update_by_id, update_time, icon, id_link, level, level_msg, name, parent_id, parent_name, path, seq,type,has_system) SELECT '1b31f8af13044cdbbad8816d355d147b', 'admin', '2021-03-23 15:34:31.743', null, null, '', 'b6b8d3c1a18c4165b5070fdf91684815_dbabc974a6344a90b6da0c7f88c9e424_1b31f8af13044cdbbad8816d355d147b', '2', '三级', '组织机构新增按钮', 'dbabc974a6344a90b6da0c7f88c9e424', '组织机构', 'organization_add', '2', '2', '1' WHERE NOT EXISTS(SELECT * FROM d1_menu_tree WHERE ID = '1b31f8af13044cdbbad8816d355d147b');
INSERT INTO d1_menu_tree (id, create_by_id, create_time, update_by_id, update_time, icon, id_link, level, level_msg, name, parent_id, parent_name, path, seq,type,has_system) SELECT '21984b06e86c4667940869dea5ac8c20', 'admin', '2021-03-19 15:10:03.372', null, null, '', 'b6b8d3c1a18c4165b5070fdf91684815_b71ead0d2db841e4b3b925d57e2b638c_21984b06e86c4667940869dea5ac8c20', '2', '三级', '编辑按钮', 'b71ead0d2db841e4b3b925d57e2b638c', '角色权限', 'role_edit', '1', '2', '1' WHERE NOT EXISTS(SELECT * FROM d1_menu_tree WHERE ID = '21984b06e86c4667940869dea5ac8c20');
INSERT INTO d1_menu_tree (id, create_by_id, create_time, update_by_id, update_time, icon, id_link, level, level_msg, name, parent_id, parent_name, path, seq,type,has_system) SELECT '2c3aa4735b484699af812281bb3f78a8', 'admin', '2021-03-19 14:53:13.806', null, null, '', 'b6b8d3c1a18c4165b5070fdf91684815_f7facbd19fcb4ae79c021f1355d03cb9_2c3aa4735b484699af812281bb3f78a8', '2', '三级', '用户管理新增按钮', 'f7facbd19fcb4ae79c021f1355d03cb9', '用户管理', 'user_add', '1', '2', '1' WHERE NOT EXISTS(SELECT * FROM d1_menu_tree WHERE ID = '2c3aa4735b484699af812281bb3f78a8');
INSERT INTO d1_menu_tree (id, create_by_id, create_time, update_by_id, update_time, icon, id_link, level, level_msg, name, parent_id, parent_name, path, seq,type,has_system) SELECT '4c16960f95974299a5fd610df6a1ed54', 'admin', '2021-03-19 18:13:46.891', null, null, '', 'ffbfa1599ff34cb8bbdbcdec6fe46b61_4c16960f95974299a5fd610df6a1ed54', '1', '二级', '操作日志', 'ffbfa1599ff34cb8bbdbcdec6fe46b61', '日志管理', '/operationLogs', '0', '0', '1' WHERE NOT EXISTS(SELECT * FROM d1_menu_tree WHERE ID = '4c16960f95974299a5fd610df6a1ed54');
INSERT INTO d1_menu_tree (id, create_by_id, create_time, update_by_id, update_time, icon, id_link, level, level_msg, name, parent_id, parent_name, path, seq,type,has_system) SELECT '4ddee9efffc84344a3d6c08726686792', 'admin', '2021-03-19 14:59:16.219', null, null, '', 'b6b8d3c1a18c4165b5070fdf91684815_f7facbd19fcb4ae79c021f1355d03cb9_4ddee9efffc84344a3d6c08726686792', '2', '三级', '用户管理批量删除按钮', 'f7facbd19fcb4ae79c021f1355d03cb9', '用户管理', 'user_batchDelete', '5', '2', '1' WHERE NOT EXISTS(SELECT * FROM d1_menu_tree WHERE ID = '4ddee9efffc84344a3d6c08726686792');
INSERT INTO d1_menu_tree (id, create_by_id, create_time, update_by_id, update_time, icon, id_link, level, level_msg, name, parent_id, parent_name, path, seq,type,has_system) SELECT '4e0a5bb8061340aaba2032fb5ce7c6f8', 'admin', '2021-03-19 11:00:56.212', null, null, '', 'b6b8d3c1a18c4165b5070fdf91684815_8fe6ba6f57954cd4a69bcb3881cd0d94_4e0a5bb8061340aaba2032fb5ce7c6f8', '2', '三级', '菜单管理批量删除按钮', '8fe6ba6f57954cd4a69bcb3881cd0d94', '菜单管理', 'menus_batchDelete', '3', '2', '1' WHERE NOT EXISTS(SELECT * FROM d1_menu_tree WHERE ID = '4e0a5bb8061340aaba2032fb5ce7c6f8');
INSERT INTO d1_menu_tree (id, create_by_id, create_time, update_by_id, update_time, icon, id_link, level, level_msg, name, parent_id, parent_name, path, seq,type,has_system) SELECT '5306c986ed644d7f93f5370c38b10a50', 'admin', '2021-03-19 15:04:18.519', null, null, '', 'b6b8d3c1a18c4165b5070fdf91684815_f7facbd19fcb4ae79c021f1355d03cb9_5306c986ed644d7f93f5370c38b10a50', '2', '三级', '批量禁用按钮', 'f7facbd19fcb4ae79c021f1355d03cb9', '用户管理', 'user_batchBan', '9', '2', '1' WHERE NOT EXISTS(SELECT * FROM d1_menu_tree WHERE ID = '5306c986ed644d7f93f5370c38b10a50');
INSERT INTO d1_menu_tree (id, create_by_id, create_time, update_by_id, update_time, icon, id_link, level, level_msg, name, parent_id, parent_name, path, seq,type,has_system) SELECT '58c067850c0c4e7e88722a0ac43668a3', 'admin', '2021-03-19 15:06:28.547', null, null, '', 'b6b8d3c1a18c4165b5070fdf91684815_f7facbd19fcb4ae79c021f1355d03cb9_58c067850c0c4e7e88722a0ac43668a3', '2', '三级', '详情按钮', 'f7facbd19fcb4ae79c021f1355d03cb9', '用户管理', 'user_detail', '11', '2', '1' WHERE NOT EXISTS(SELECT * FROM d1_menu_tree WHERE ID = '58c067850c0c4e7e88722a0ac43668a3');
INSERT INTO d1_menu_tree (id, create_by_id, create_time, update_by_id, update_time, icon, id_link, level, level_msg, name, parent_id, parent_name, path, seq,type,has_system) SELECT '68f58248669b406984663e6b7539dc4f', 'admin', '2021-03-19 15:16:17.853', null, null, '', 'b6b8d3c1a18c4165b5070fdf91684815_b71ead0d2db841e4b3b925d57e2b638c_68f58248669b406984663e6b7539dc4f', '2', '三级', '权限分配按钮', 'b71ead0d2db841e4b3b925d57e2b638c', '角色权限', 'role_permissionAssignment', '4', '2', '1' WHERE NOT EXISTS(SELECT * FROM d1_menu_tree WHERE ID = '68f58248669b406984663e6b7539dc4f');
INSERT INTO d1_menu_tree (id, create_by_id, create_time, update_by_id, update_time, icon, id_link, level, level_msg, name, parent_id, parent_name, path, seq,type,has_system) SELECT '6ef3337c0eba4df2ab099811cff61674', 'admin', '2021-03-19 18:17:21.141', null, null, '', '96050fae0d674f3998ee81a3556b3164_6ef3337c0eba4df2ab099811cff61674', '1', '二级', '服务器信息', '96050fae0d674f3998ee81a3556b3164', '系统设置', '/service', '0', '0', '1' WHERE NOT EXISTS(SELECT * FROM d1_menu_tree WHERE ID = '6ef3337c0eba4df2ab099811cff61674');
INSERT INTO d1_menu_tree (id, create_by_id, create_time, update_by_id, update_time, icon, id_link, level, level_msg, name, parent_id, parent_name, path, seq,type,has_system) SELECT '81271ed483d54ddb814b8c72de9e7b04', 'admin', '2021-03-19 18:15:25.983', 'admin', '2021-03-23 16:46:14.403', 'infoManage', '81271ed483d54ddb814b8c72de9e7b04', '0', '一级', '消息管理', '', null, '', '1', '0', '1' WHERE NOT EXISTS(SELECT * FROM d1_menu_tree WHERE ID = '81271ed483d54ddb814b8c72de9e7b04');
INSERT INTO d1_menu_tree (id, create_by_id, create_time, update_by_id, update_time, icon, id_link, level, level_msg, name, parent_id, parent_name, path, seq,type,has_system) SELECT '8fe6ba6f57954cd4a69bcb3881cd0d94', 'admin', '2021-03-19 10:58:39.846', 'admin', '2021-03-23 08:01:40.672', '', 'b6b8d3c1a18c4165b5070fdf91684815_8fe6ba6f57954cd4a69bcb3881cd0d94', '1', '二级', '菜单管理', 'b6b8d3c1a18c4165b5070fdf91684815', '系统管理', '/menus', '1', '0', '1' WHERE NOT EXISTS(SELECT * FROM d1_menu_tree WHERE ID = '8fe6ba6f57954cd4a69bcb3881cd0d94');
INSERT INTO d1_menu_tree (id, create_by_id, create_time, update_by_id, update_time, icon, id_link, level, level_msg, name, parent_id, parent_name, path, seq,type,has_system) SELECT '951c7103f1394631931a8badd48b5975', 'admin', '2021-03-19 18:15:54.151', null, null, '', '81271ed483d54ddb814b8c72de9e7b04_951c7103f1394631931a8badd48b5975', '1', '二级', '消息管理列表', '81271ed483d54ddb814b8c72de9e7b04', '消息管理', '/messages', '0', '0', '1' WHERE NOT EXISTS(SELECT * FROM d1_menu_tree WHERE ID = '951c7103f1394631931a8badd48b5975');
INSERT INTO d1_menu_tree (id, create_by_id, create_time, update_by_id, update_time, icon, id_link, level, level_msg, name, parent_id, parent_name, path, seq,type,has_system) SELECT '96050fae0d674f3998ee81a3556b3164', 'admin', '2021-03-19 18:16:48.609', 'admin', '2021-03-23 16:46:23.828', 'sysSetup', '96050fae0d674f3998ee81a3556b3164', '0', '一级', '系统设置', '', null, '', '3', '0', '1' WHERE NOT EXISTS(SELECT * FROM d1_menu_tree WHERE ID = '96050fae0d674f3998ee81a3556b3164');
INSERT INTO d1_menu_tree (id, create_by_id, create_time, update_by_id, update_time, icon, id_link, level, level_msg, name, parent_id, parent_name, path, seq,type,has_system) SELECT '9a6d1f16e306467bb96166de6cc80159', 'admin', '2021-03-19 11:00:29.979', null, null, '', 'b6b8d3c1a18c4165b5070fdf91684815_8fe6ba6f57954cd4a69bcb3881cd0d94_9a6d1f16e306467bb96166de6cc80159', '2', '三级', '菜单管理删除按钮', '8fe6ba6f57954cd4a69bcb3881cd0d94', '菜单管理', 'menus_delete', '2', '2', '1' WHERE NOT EXISTS(SELECT * FROM d1_menu_tree WHERE ID = '9a6d1f16e306467bb96166de6cc80159');
INSERT INTO d1_menu_tree (id, create_by_id, create_time, update_by_id, update_time, icon, id_link, level, level_msg, name, parent_id, parent_name, path, seq,type,has_system) SELECT '9f911a785e2d49c682dc22d0379e0455', 'admin', '2021-03-19 15:15:22.112', null, null, '', 'b6b8d3c1a18c4165b5070fdf91684815_b71ead0d2db841e4b3b925d57e2b638c_9f911a785e2d49c682dc22d0379e0455', '2', '三级', '用户列表按钮', 'b71ead0d2db841e4b3b925d57e2b638c', '角色权限', 'role_userList', '3', '2', '1' WHERE NOT EXISTS(SELECT * FROM d1_menu_tree WHERE ID = '9f911a785e2d49c682dc22d0379e0455');
INSERT INTO d1_menu_tree (id, create_by_id, create_time, update_by_id, update_time, icon, id_link, level, level_msg, name, parent_id, parent_name, path, seq,type,has_system) SELECT 'af1c183e6b9e4ee3970aecb62670ebf8', 'admin', '2021-03-19 14:53:48.776', null, null, '', 'b6b8d3c1a18c4165b5070fdf91684815_f7facbd19fcb4ae79c021f1355d03cb9_af1c183e6b9e4ee3970aecb62670ebf8', '2', '三级', '用户管理编辑按钮', 'f7facbd19fcb4ae79c021f1355d03cb9', '用户管理', 'user_edit', '2', '2', '1' WHERE NOT EXISTS(SELECT * FROM d1_menu_tree WHERE ID = 'af1c183e6b9e4ee3970aecb62670ebf8');
INSERT INTO d1_menu_tree (id, create_by_id, create_time, update_by_id, update_time, icon, id_link, level, level_msg, name, parent_id, parent_name, path, seq,type,has_system) SELECT 'b52942bcd7d54a378dc4b61661ba1a4b', 'admin', '2021-03-19 14:48:13.268', null, null, '', 'b6b8d3c1a18c4165b5070fdf91684815_dbabc974a6344a90b6da0c7f88c9e424_b52942bcd7d54a378dc4b61661ba1a4b', '2', '三级', '组织机构删除按钮', 'dbabc974a6344a90b6da0c7f88c9e424', '组织机构', 'organization_delete', '1', '2', '1' WHERE NOT EXISTS(SELECT * FROM d1_menu_tree WHERE ID = 'b52942bcd7d54a378dc4b61661ba1a4b');
INSERT INTO d1_menu_tree (id, create_by_id, create_time, update_by_id, update_time, icon, id_link, level, level_msg, name, parent_id, parent_name, path, seq,type,has_system) SELECT 'b6b8d3c1a18c4165b5070fdf91684815', 'admin', '2021-03-19 10:58:20.419', 'admin', '2021-03-23 16:46:07.478', 'sysManage', 'b6b8d3c1a18c4165b5070fdf91684815', '0', '一级', '系统管理', '', null, '', '0', '0', '1' WHERE NOT EXISTS(SELECT * FROM d1_menu_tree WHERE ID = 'b6b8d3c1a18c4165b5070fdf91684815');
INSERT INTO d1_menu_tree (id, create_by_id, create_time, update_by_id, update_time, icon, id_link, level, level_msg, name, parent_id, parent_name, path, seq,type,has_system) SELECT 'b71ead0d2db841e4b3b925d57e2b638c', 'admin', '2021-03-19 15:08:21.105', 'admin', '2021-03-23 08:13:03.154', '', 'b6b8d3c1a18c4165b5070fdf91684815_b71ead0d2db841e4b3b925d57e2b638c', '1', '二级', '角色权限', 'b6b8d3c1a18c4165b5070fdf91684815', '系统管理', '/role', '3', '0', '1' WHERE NOT EXISTS(SELECT * FROM d1_menu_tree WHERE ID = 'b71ead0d2db841e4b3b925d57e2b638c');
INSERT INTO d1_menu_tree (id, create_by_id, create_time, update_by_id, update_time, icon, id_link, level, level_msg, name, parent_id, parent_name, path, seq,type,has_system) SELECT 'c700dcd3cab441148c81c07ce3c1ef69', 'admin', '2021-03-19 15:00:24.105', null, null, '', 'b6b8d3c1a18c4165b5070fdf91684815_f7facbd19fcb4ae79c021f1355d03cb9_c700dcd3cab441148c81c07ce3c1ef69', '2', '三级', '用户管理用户导出按钮', 'f7facbd19fcb4ae79c021f1355d03cb9', '用户管理', 'user_userExport', '6', '2', '1' WHERE NOT EXISTS(SELECT * FROM d1_menu_tree WHERE ID = 'c700dcd3cab441148c81c07ce3c1ef69');
INSERT INTO d1_menu_tree (id, create_by_id, create_time, update_by_id, update_time, icon, id_link, level, level_msg, name, parent_id, parent_name, path, seq,type,has_system) SELECT 'cb38c7fad3eb45bfbf9422cc24b62178', 'admin', '2021-03-19 10:59:21.348', null, null, '', 'b6b8d3c1a18c4165b5070fdf91684815_8fe6ba6f57954cd4a69bcb3881cd0d94_cb38c7fad3eb45bfbf9422cc24b62178', '2', '三级', '菜单管理新增按钮', '8fe6ba6f57954cd4a69bcb3881cd0d94', '菜单管理', 'menus_add', '0', '2', '1' WHERE NOT EXISTS(SELECT * FROM d1_menu_tree WHERE ID = 'cb38c7fad3eb45bfbf9422cc24b62178');
INSERT INTO d1_menu_tree (id, create_by_id, create_time, update_by_id, update_time, icon, id_link, level, level_msg, name, parent_id, parent_name, path, seq,type,has_system) SELECT 'd130850df56e44329bd24814bf2f8902', 'admin', '2021-03-19 14:55:38.224', null, null, '', 'b6b8d3c1a18c4165b5070fdf91684815_f7facbd19fcb4ae79c021f1355d03cb9_d130850df56e44329bd24814bf2f8902', '2', '三级', '用户管理模板下载按钮', 'f7facbd19fcb4ae79c021f1355d03cb9', '用户管理', 'user_templateDownload', '3', '2', '1' WHERE NOT EXISTS(SELECT * FROM d1_menu_tree WHERE ID = 'd130850df56e44329bd24814bf2f8902');
INSERT INTO d1_menu_tree (id, create_by_id, create_time, update_by_id, update_time, icon, id_link, level, level_msg, name, parent_id, parent_name, path, seq,type,has_system) SELECT 'd219c621546c4468901f38ab3b8ce317', 'admin', '2021-03-19 14:52:28.6', null, null, '', 'b6b8d3c1a18c4165b5070fdf91684815_f7facbd19fcb4ae79c021f1355d03cb9_d219c621546c4468901f38ab3b8ce317', '2', '三级', '用户管理新增', 'f7facbd19fcb4ae79c021f1355d03cb9', '用户管理', 'user_add', '0', '2', '1' WHERE NOT EXISTS(SELECT * FROM d1_menu_tree WHERE ID = 'd219c621546c4468901f38ab3b8ce317');
INSERT INTO d1_menu_tree (id, create_by_id, create_time, update_by_id, update_time, icon, id_link, level, level_msg, name, parent_id, parent_name, path, seq,type,has_system) SELECT 'dbabc974a6344a90b6da0c7f88c9e424', 'admin', '2021-03-19 11:03:19.695', 'admin', '2021-03-23 08:01:48.915', '', 'b6b8d3c1a18c4165b5070fdf91684815_dbabc974a6344a90b6da0c7f88c9e424', '1', '二级', '组织机构', 'b6b8d3c1a18c4165b5070fdf91684815', '系统管理', '/organization', '2', '0', '1' WHERE NOT EXISTS(SELECT * FROM d1_menu_tree WHERE ID = 'dbabc974a6344a90b6da0c7f88c9e424');
INSERT INTO d1_menu_tree (id, create_by_id, create_time, update_by_id, update_time, icon, id_link, level, level_msg, name, parent_id, parent_name, path, seq,type,has_system) SELECT 'df88a57d5ab84bc58899a3ebcdbf3c4e', 'admin', '2021-03-19 15:14:30.039', null, null, '', 'b6b8d3c1a18c4165b5070fdf91684815_b71ead0d2db841e4b3b925d57e2b638c_df88a57d5ab84bc58899a3ebcdbf3c4e', '2', '三级', '角色删除按钮', 'b71ead0d2db841e4b3b925d57e2b638c', '角色权限', 'role_delete', '2', '2', '1' WHERE NOT EXISTS(SELECT * FROM d1_menu_tree WHERE ID = 'df88a57d5ab84bc58899a3ebcdbf3c4e');
INSERT INTO d1_menu_tree (id, create_by_id, create_time, update_by_id, update_time, icon, id_link, level, level_msg, name, parent_id, parent_name, path, seq,type,has_system) SELECT 'f11f28ac9f744eb69b05be651d10f5ff', 'admin', '2021-03-19 14:58:25.188', 'admin', '2021-03-19 16:57:57.218', '', 'b6b8d3c1a18c4165b5070fdf91684815_f7facbd19fcb4ae79c021f1355d03cb9_f11f28ac9f744eb69b05be651d10f5ff', '2', '三级', '用户管理批量重置密码按钮', 'f7facbd19fcb4ae79c021f1355d03cb9', '用户管理', 'user_batchRetPassword', '4', '2', '1' WHERE NOT EXISTS(SELECT * FROM d1_menu_tree WHERE ID = 'f11f28ac9f744eb69b05be651d10f5ff');
INSERT INTO d1_menu_tree (id, create_by_id, create_time, update_by_id, update_time, icon, id_link, level, level_msg, name, parent_id, parent_name, path, seq,type,has_system) SELECT 'f72f39e2b35c4e4ba26dd02d9603f7a2', 'admin', '2021-03-19 15:03:46.714', null, null, '', 'b6b8d3c1a18c4165b5070fdf91684815_f7facbd19fcb4ae79c021f1355d03cb9_f72f39e2b35c4e4ba26dd02d9603f7a2', '2', '三级', '批量启用按钮', 'f7facbd19fcb4ae79c021f1355d03cb9', '用户管理', 'user_batchEnable', '8', '2', '1' WHERE NOT EXISTS(SELECT * FROM d1_menu_tree WHERE ID = 'f72f39e2b35c4e4ba26dd02d9603f7a2');
INSERT INTO d1_menu_tree (id, create_by_id, create_time, update_by_id, update_time, icon, id_link, level, level_msg, name, parent_id, parent_name, path, seq,type,has_system) SELECT 'f7facbd19fcb4ae79c021f1355d03cb9', 'admin', '2021-03-19 14:50:56.401', null, null, '', 'b6b8d3c1a18c4165b5070fdf91684815_f7facbd19fcb4ae79c021f1355d03cb9', '1', '二级', '用户管理', 'b6b8d3c1a18c4165b5070fdf91684815', '系统管理', '/user', '0', '0', '1' WHERE NOT EXISTS(SELECT * FROM d1_menu_tree WHERE ID = 'f7facbd19fcb4ae79c021f1355d03cb9');
INSERT INTO d1_menu_tree (id, create_by_id, create_time, update_by_id, update_time, icon, id_link, level, level_msg, name, parent_id, parent_name, path, seq,type,has_system) SELECT 'f887432193e9422c90ea1ad25247b877', 'admin', '2021-03-19 15:09:24.627', null, null, '', 'b6b8d3c1a18c4165b5070fdf91684815_b71ead0d2db841e4b3b925d57e2b638c_f887432193e9422c90ea1ad25247b877', '2', '三级', '新增按钮', 'b71ead0d2db841e4b3b925d57e2b638c', '角色权限', 'role_add', '0', '2', '1' WHERE NOT EXISTS(SELECT * FROM d1_menu_tree WHERE ID = 'f887432193e9422c90ea1ad25247b877');
INSERT INTO d1_menu_tree (id, create_by_id, create_time, update_by_id, update_time, icon, id_link, level, level_msg, name, parent_id, parent_name, path, seq,type,has_system) SELECT 'f9582a6d01834919a6cd20315ae6e883', 'admin', '2021-03-19 10:59:56.977', null, null, '', 'b6b8d3c1a18c4165b5070fdf91684815_8fe6ba6f57954cd4a69bcb3881cd0d94_f9582a6d01834919a6cd20315ae6e883', '2', '三级', '菜单管理编辑按钮', '8fe6ba6f57954cd4a69bcb3881cd0d94', '菜单管理', 'menus_edit', '1', '2', '1' WHERE NOT EXISTS(SELECT * FROM d1_menu_tree WHERE ID = 'f9582a6d01834919a6cd20315ae6e883');
INSERT INTO d1_menu_tree (id, create_by_id, create_time, update_by_id, update_time, icon, id_link, level, level_msg, name, parent_id, parent_name, path, seq,type,has_system) SELECT 'ffbfa1599ff34cb8bbdbcdec6fe46b61', 'admin', '2021-03-19 18:13:30.948', 'admin', '2021-03-23 16:46:19.084', 'logManage', 'ffbfa1599ff34cb8bbdbcdec6fe46b61', '0', '一级', '日志管理', '', null, '', '2', '0', '1' WHERE NOT EXISTS(SELECT * FROM d1_menu_tree WHERE ID = 'ffbfa1599ff34cb8bbdbcdec6fe46b61');

INSERT INTO d1_org_ex (id, name, org_code) SELECT '1','发改委','hb_fgw' WHERE NOT EXISTS(SELECT * FROM d1_org_ex WHERE ID = '1');
INSERT INTO d1_org_ex (id, name, org_code) SELECT '10','商务局','hb_swj' WHERE NOT EXISTS(SELECT * FROM d1_org_ex WHERE ID = '10');
INSERT INTO d1_org_ex (id, name, org_code) SELECT '11','教育局','hb_jyj' WHERE NOT EXISTS(SELECT * FROM d1_org_ex WHERE ID = '11');
INSERT INTO d1_org_ex (id, name, org_code) SELECT '12','财政局','hb_czj' WHERE NOT EXISTS(SELECT * FROM d1_org_ex WHERE ID = '12');
INSERT INTO d1_org_ex (id, name, org_code) SELECT '13','审计局','hb_sjj' WHERE NOT EXISTS(SELECT * FROM d1_org_ex WHERE ID = '13');
INSERT INTO d1_org_ex (id, name, org_code) SELECT '14','民政局','hb_mzj' WHERE NOT EXISTS(SELECT * FROM d1_org_ex WHERE ID = '14');
INSERT INTO d1_org_ex (id, name, org_code) SELECT '15','人社局','hb_sbj' WHERE NOT EXISTS(SELECT * FROM d1_org_ex WHERE ID = '15');
INSERT INTO d1_org_ex (id, name, org_code) SELECT '16','科技局','hb_kjj' WHERE NOT EXISTS(SELECT * FROM d1_org_ex WHERE ID = '16');
INSERT INTO d1_org_ex (id, name, org_code) SELECT '17','应急局','hb_yjj' WHERE NOT EXISTS(SELECT * FROM d1_org_ex WHERE ID = '17');
INSERT INTO d1_org_ex (id, name, org_code) SELECT '18','司法局','hb_sfj' WHERE NOT EXISTS(SELECT * FROM d1_org_ex WHERE ID = '18');
INSERT INTO d1_org_ex (id, name, org_code) SELECT '19','文旅局','hb_wlj' WHERE NOT EXISTS(SELECT * FROM d1_org_ex WHERE ID = '19');
INSERT INTO d1_org_ex (id, name, org_code) SELECT '2','城管委','hb_cgw' WHERE NOT EXISTS(SELECT * FROM d1_org_ex WHERE ID = '2');
INSERT INTO d1_org_ex (id, name, org_code) SELECT '20','生态环境局','hb_stj' WHERE NOT EXISTS(SELECT * FROM d1_org_ex WHERE ID = '20');
INSERT INTO d1_org_ex (id, name, org_code) SELECT '22','体育局','hb_tyj' WHERE NOT EXISTS(SELECT * FROM d1_org_ex WHERE ID = '22');
INSERT INTO d1_org_ex (id, name, org_code) SELECT '23','统计局','hb_tjj' WHERE NOT EXISTS(SELECT * FROM d1_org_ex WHERE ID = '23');
INSERT INTO d1_org_ex (id, name, org_code) SELECT '24','医保局','hb_ybj' WHERE NOT EXISTS(SELECT * FROM d1_org_ex WHERE ID = '24');
INSERT INTO d1_org_ex (id, name, org_code) SELECT '25','退役军人局','hb_tyjrj' WHERE NOT EXISTS(SELECT * FROM d1_org_ex WHERE ID = '25');
INSERT INTO d1_org_ex (id, name, org_code) SELECT '26','市场监管局','hb_scjgj' WHERE NOT EXISTS(SELECT * FROM d1_org_ex WHERE ID = '26');
INSERT INTO d1_org_ex (id, name, org_code) SELECT '27','公安分局','hb_gaj' WHERE NOT EXISTS(SELECT * FROM d1_org_ex WHERE ID = '27');
INSERT INTO d1_org_ex (id, name, org_code) SELECT '28','规划分局','hb_gjz' WHERE NOT EXISTS(SELECT * FROM d1_org_ex WHERE ID = '28');
INSERT INTO d1_org_ex (id, name, org_code) SELECT '29','江都路街','hb_jdljd' WHERE NOT EXISTS(SELECT * FROM d1_org_ex WHERE ID = '29');
INSERT INTO d1_org_ex (id, name, org_code) SELECT '3','住建委','hb_zjw' WHERE NOT EXISTS(SELECT * FROM d1_org_ex WHERE ID = '3');
INSERT INTO d1_org_ex (id, name, org_code) SELECT '30','铁东路街','hb_tdljd' WHERE NOT EXISTS(SELECT * FROM d1_org_ex WHERE ID = '30');
INSERT INTO d1_org_ex (id, name, org_code) SELECT '31','宁园街','hb_nyjd' WHERE NOT EXISTS(SELECT * FROM d1_org_ex WHERE ID = '31');
INSERT INTO d1_org_ex (id, name, org_code) SELECT '32','王串场街','hb_wccjd' WHERE NOT EXISTS(SELECT * FROM d1_org_ex WHERE ID = '32');
INSERT INTO d1_org_ex (id, name, org_code) SELECT '33','月牙河街','hb_yahjd' WHERE NOT EXISTS(SELECT * FROM d1_org_ex WHERE ID = '33');
INSERT INTO d1_org_ex (id, name, org_code) SELECT '34','鸿顺里街','hb_hsljd' WHERE NOT EXISTS(SELECT * FROM d1_org_ex WHERE ID = '34');
INSERT INTO d1_org_ex (id, name, org_code) SELECT '35','建昌道街','hb_jcdjd' WHERE NOT EXISTS(SELECT * FROM d1_org_ex WHERE ID = '35');
INSERT INTO d1_org_ex (id, name, org_code) SELECT '36','光复道街','hb_gfdjd' WHERE NOT EXISTS(SELECT * FROM d1_org_ex WHERE ID = '36');
INSERT INTO d1_org_ex (id, name, org_code) SELECT '37','新开河街','hb_xkhjd' WHERE NOT EXISTS(SELECT * FROM d1_org_ex WHERE ID = '37');
INSERT INTO d1_org_ex (id, name, org_code) SELECT '38','望海楼街','hb_whljd' WHERE NOT EXISTS(SELECT * FROM d1_org_ex WHERE ID = '38');
INSERT INTO d1_org_ex (id, name, org_code) SELECT '39','网信办','hb-wxb' WHERE NOT EXISTS(SELECT * FROM d1_org_ex WHERE ID = '39');
INSERT INTO d1_org_ex (id, name, org_code) SELECT '4','卫健委','hb_wjw' WHERE NOT EXISTS(SELECT * FROM d1_org_ex WHERE ID = '4');
INSERT INTO d1_org_ex (id, name, org_code) SELECT '40','政府办','hb_zfb' WHERE NOT EXISTS(SELECT * FROM d1_org_ex WHERE ID = '40');
INSERT INTO d1_org_ex (id, name, org_code) SELECT '41','团委','hb_gqt' WHERE NOT EXISTS(SELECT * FROM d1_org_ex WHERE ID = '41');
INSERT INTO d1_org_ex (id, name, org_code) SELECT '42','档案馆','hb_dag' WHERE NOT EXISTS(SELECT * FROM d1_org_ex WHERE ID = '42');
INSERT INTO d1_org_ex (id, name, org_code) SELECT '43','残联','hb_cl' WHERE NOT EXISTS(SELECT * FROM d1_org_ex WHERE ID = '43');
INSERT INTO d1_org_ex (id, name, org_code) SELECT '44','工商联','hb_gsl' WHERE NOT EXISTS(SELECT * FROM d1_org_ex WHERE ID = '44');
INSERT INTO d1_org_ex (id, name, org_code) SELECT '45','红十字会','hb_hsz' WHERE NOT EXISTS(SELECT * FROM d1_org_ex WHERE ID = '45');
INSERT INTO d1_org_ex (id, name, org_code) SELECT '46','融媒体','hb_rmt' WHERE NOT EXISTS(SELECT * FROM d1_org_ex WHERE ID = '46');
INSERT INTO d1_org_ex (id, name, org_code) SELECT '47','党校','hb_dx' WHERE NOT EXISTS(SELECT * FROM d1_org_ex WHERE ID = '47');
INSERT INTO d1_org_ex (id, name, org_code) SELECT '48','编办','hb_bb' WHERE NOT EXISTS(SELECT * FROM d1_org_ex WHERE ID = '48');
INSERT INTO d1_org_ex (id, name, org_code) SELECT '49','总工会','hb_zgh' WHERE NOT EXISTS(SELECT * FROM d1_org_ex WHERE ID = '49');
INSERT INTO d1_org_ex (id, name, org_code) SELECT '5','国资委','hb_gzw' WHERE NOT EXISTS(SELECT * FROM d1_org_ex WHERE ID = '5');
INSERT INTO d1_org_ex (id, name, org_code) SELECT '50','消防支队','hb_xf' WHERE NOT EXISTS(SELECT * FROM d1_org_ex WHERE ID = '50');
INSERT INTO d1_org_ex (id, name, org_code) SELECT '6','政务服务办','hb_zwb' WHERE NOT EXISTS(SELECT * FROM d1_org_ex WHERE ID = '6');
INSERT INTO d1_org_ex (id, name, org_code) SELECT '8','人防办','hb_rfb' WHERE NOT EXISTS(SELECT * FROM d1_org_ex WHERE ID = '8');
INSERT INTO d1_org_ex (id, name, org_code) SELECT '9','楼宇办','hb_lyb' WHERE NOT EXISTS(SELECT * FROM d1_org_ex WHERE ID = '9');

