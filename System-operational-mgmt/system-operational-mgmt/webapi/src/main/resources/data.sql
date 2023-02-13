-- 初始化数据库库表数据
--1. 管理用户初始化一个admin用户
INSERT INTO d1_web_admin_user (ID,NAME, PASSWORD,tel,role_Id,role_Name) SELECT
	'admin',
	'admin',
	'e10adc3949ba59abbe56e057f20f883e',
	'admin',
	'admin',
	'超级管理员'
WHERE
	NOT EXISTS (
		SELECT
			*
		FROM
			d1_web_admin_user
		WHERE
			ID = 'admin'
	);
--2. 初始化菜单
-- INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '1','首页','','/dashboard','menu','1','iconfont icon-tuanduicankaoxian-' WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '1');
-- INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '2','知识库','','2','menu','2','iconfont icon-zhishizhongxin' WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '2');
-- INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '2-1','模型管理','2','/index/model','menu','0',''  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '2-1');
-- INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '2-2','数学模型建模','2','/index/dataModel','menu','1',''  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '2-2');
-- INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '3','大数据处理服务','','3','menu','3','iconfont icon-25'  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '3');
-- INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '3-1','数学模型执行引擎','3','/dataProcessingService/digitalModel','menu','0',''  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '3-1');
-- INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '3-2','接口管理','3','/dataProcessingService/apiManageView','menu','1',''  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '3-2');
-- INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '3-3','数据处理服务','3','/dataProcessingService/dataProcessServiceView','menu','2',''  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '3-3');
--
-- INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '3-4','批量安装部署工具','3','/deploymentView/configView','menu','3',''  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '3-4');
INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '1','用户行为监控','','1','menu','1','iconfont icon-yonghushezhi-'  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '1');
INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '1-1','用户登录信息监控','1','/behavior/userLogin','menu','0',''  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '1-1');
INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '1-2','在线用户监控','1','/behavior/online','menu','1',''  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '1-2');
INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '1-3','用户活跃度','1','/behavior/activity','menu','2',''  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '1-3');
INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '1-4','操作日志','1','/omMager/log','menu','3',''  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '1-4');

INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '2','运维行为管理','','2','menu','2','iconfont icon-yunweiguanli'  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '2');
INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '2-1','组件管理','2','/omMager/comments','menu','0',''  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '2-1');
INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '2-2','日志管理','2','/omMager/log','menu','1',''  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '2-2');
-- INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '4-1','用户管理','4','/omMager/user','menu','0',''  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '4-1');

INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '3','运维审批管理','','3','menu','3','iconfont icon-yunweiguanli'  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '3');
INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '3-1','审批管理','3','/eaMager/approve','menu','0',''  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '3-1');

INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '4','资源统一监控','','4','menu','4','iconfont icon-wulianwang-'  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '4');
INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '4-1','基础环境监控','4','4-1','menu','0',''  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '4-1');
INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '4-1-1','基础环境监控','4-1','/monitoringView/environmentView','menu','0',''  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '4-1-1');
INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '4-1-2','系统监控','4-1','/monitoringView/systemView','menu','1',''  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '4-1-2');
INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '4-1-3','数据监控','4-1','/monitoringView/dataView','menu','2',''  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '4-1-3');
INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '4-1-4','资源监控','4-1','/monitoringView/sourceView','menu','3',''  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '4-1-4');

INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '4-2','模块监控','4','4-2','menu','1','iconfont icon-zuoce-zonghejiankong'  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '4-2');
INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '4-2-1','模块访问统计','4-2','/moduleMonitorView/moduleStatisticsView','menu','0',''  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '4-2-1');
INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '4-2-2','平台性能监控','4-2','/moduleMonitorView/performanceView','menu','1',''  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '4-2-2');
INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '4-2-3','数据监控','4-2','/moduleMonitorView/dataMonitorView','menu','2',''  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '4-2-3');
INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '4-2-4','资源监控','4-2','/moduleMonitorView/resourceView','menu','3',''  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '4-2-4');

-- INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '7-5','异常报警','7','/moduleMonitorView/resourceView','menu','4',''  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '7-5');

INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '5','数据库运维','','5','menu','5','iconfont icon-yunshujukuRedis'  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '5');
INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '5-1','参数配置','5','/dataBase/parameter','menu','0',''  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '5-1');
INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '5-2','实例管理','5','/dataBase/examples','menu','1',''  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '5-2');
INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '5-3','数据库监控','5','/dataBase/dataMonitoring','menu','2',''  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '5-3');
INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '5-4','备份和恢复','5','/dataBase/copy','menu','3',''  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '5-4');
INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '5-5','远程SQL','5','/dataBase/sql','menu','4',''  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '5-5');

INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '6','运维指令管理','','6','menu','6','iconfont icon-Icon-yuanchengxiezuo'  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '6');
INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '6-1','运维指令分类','6','/longRangeControlView/operationalClassView','menu','0',''  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '6-1');
INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '6-2','日常运维指令','6','/longRangeControlView/operationalDayView','menu','1',''  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '6-2');
INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '6-3','远程控制台','6','/longRangeControlView/remoteConsoleView','menu','2',''  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '6-3');

INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '7','系统管理','','7','menu','7','iconfont icon-zhinengsuo'  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '7');
INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '7-1','用户管理','7','/system/user','menu','0',''  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '7-1');
-- INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '7-2','权限管理','7','/system/permissionView','menu','1',''  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '7-2');
-- INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '7-3','菜单管理','7','/system/menuTreeView','menu','2',''  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '7-3');
-- INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '7-4','组织机构','7','/system/deptView','menu','3',''  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '7-4');




-- INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '9-4','指令运行跟踪','9','/longRangeControlView/remoteConsoleView','menu','3',''  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '9-4');

-- INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '10','自动化部署','','10','menu','10','iconfont icon-zhinengsuo'  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '10');
-- INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '10-1','安装路径管理','10','/deploymentView/installationPathView','menu','0',''  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '10-1');
-- INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '10-2','版本管理及部署','10','/deploymentView/versionView','menu','1',''  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '10-2');
-- INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '10-3','补丁管理及部署','10','/deploymentView/patchView','menu','2',''  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '10-3');
-- INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '10-4','配置管理及部署','10','/deploymentView/configView','menu','3',''  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '10-4');
-- INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '10-5','部署进度跟踪','10','/deploymentView/deploymentLogView','menu','4',''  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '10-5');

-- INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '10-6','版本回滚','10','/deploymentView/versionView','menu','3',''  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '10-6');
-- INSERT INTO d1_menu_tree (id, name, parent_id,route,type,order_num,icon) SELECT '10-7','自动更新','10','/deploymentView/configView','menu','5',''  WHERE NOT EXISTS ( SELECT * FROM d1_menu_tree WHERE id = '10-7');
--3. 初始化管理员角色
INSERT INTO d1_role (id, name, create_time) SELECT 'admin','超级管理员',now()  WHERE NOT EXISTS ( SELECT * FROM d1_role WHERE id = 'admin');
--4. 初始化管理员角色权限
INSERT INTO d1_role_permission (id, role_id, menu_tree_id) SELECT '1','admin','1'  WHERE NOT EXISTS ( SELECT * FROM d1_role_permission WHERE id = '1');
INSERT INTO d1_role_permission (id, role_id, menu_tree_id) SELECT '2','admin','1-1'  WHERE NOT EXISTS ( SELECT * FROM d1_role_permission WHERE id = '2');
INSERT INTO d1_role_permission (id, role_id, menu_tree_id) SELECT '3','admin','1-2'  WHERE NOT EXISTS ( SELECT * FROM d1_role_permission WHERE id = '3');
INSERT INTO d1_role_permission (id, role_id, menu_tree_id) SELECT '4','admin','1-3'  WHERE NOT EXISTS ( SELECT * FROM d1_role_permission WHERE id = '4');
INSERT INTO d1_role_permission (id, role_id, menu_tree_id) SELECT '5','admin','1-4'  WHERE NOT EXISTS ( SELECT * FROM d1_role_permission WHERE id = '5');
INSERT INTO d1_role_permission (id, role_id, menu_tree_id) SELECT '6','admin','2'  WHERE NOT EXISTS ( SELECT * FROM d1_role_permission WHERE id = '6');
INSERT INTO d1_role_permission (id, role_id, menu_tree_id) SELECT '7','admin','2-1'  WHERE NOT EXISTS ( SELECT * FROM d1_role_permission WHERE id = '7');
INSERT INTO d1_role_permission (id, role_id, menu_tree_id) SELECT '8','admin','2-2'  WHERE NOT EXISTS ( SELECT * FROM d1_role_permission WHERE id = '8');
INSERT INTO d1_role_permission (id, role_id, menu_tree_id) SELECT '9','admin','3'  WHERE NOT EXISTS ( SELECT * FROM d1_role_permission WHERE id = '9');
INSERT INTO d1_role_permission (id, role_id, menu_tree_id) SELECT '10','admin','3-1'  WHERE NOT EXISTS ( SELECT * FROM d1_role_permission WHERE id = '10');
INSERT INTO d1_role_permission (id, role_id, menu_tree_id) SELECT '11','admin','4'  WHERE NOT EXISTS ( SELECT * FROM d1_role_permission WHERE id = '11');
INSERT INTO d1_role_permission (id, role_id, menu_tree_id) SELECT '12','admin','4-1'  WHERE NOT EXISTS ( SELECT * FROM d1_role_permission WHERE id = '12');
INSERT INTO d1_role_permission (id, role_id, menu_tree_id) SELECT '13','admin','4-1-1'  WHERE NOT EXISTS ( SELECT * FROM d1_role_permission WHERE id = '13');
INSERT INTO d1_role_permission (id, role_id, menu_tree_id) SELECT '14','admin','4-1-2'  WHERE NOT EXISTS ( SELECT * FROM d1_role_permission WHERE id = '14');
INSERT INTO d1_role_permission (id, role_id, menu_tree_id) SELECT '15','admin','4-1-3'  WHERE NOT EXISTS ( SELECT * FROM d1_role_permission WHERE id = '15');
INSERT INTO d1_role_permission (id, role_id, menu_tree_id) SELECT '16','admin','4-1-4'  WHERE NOT EXISTS ( SELECT * FROM d1_role_permission WHERE id = '16');
INSERT INTO d1_role_permission (id, role_id, menu_tree_id) SELECT '17','admin','4-2'  WHERE NOT EXISTS ( SELECT * FROM d1_role_permission WHERE id = '17');
INSERT INTO d1_role_permission (id, role_id, menu_tree_id) SELECT '18','admin','4-2-1'  WHERE NOT EXISTS ( SELECT * FROM d1_role_permission WHERE id = '18');
INSERT INTO d1_role_permission (id, role_id, menu_tree_id) SELECT '19','admin','4-2-2'  WHERE NOT EXISTS ( SELECT * FROM d1_role_permission WHERE id = '19');
INSERT INTO d1_role_permission (id, role_id, menu_tree_id) SELECT '20','admin','4-2-3'  WHERE NOT EXISTS ( SELECT * FROM d1_role_permission WHERE id = '20');
INSERT INTO d1_role_permission (id, role_id, menu_tree_id) SELECT '21','admin','4-2-4'  WHERE NOT EXISTS ( SELECT * FROM d1_role_permission WHERE id = '21');
INSERT INTO d1_role_permission (id, role_id, menu_tree_id) SELECT '22','admin','5'  WHERE NOT EXISTS ( SELECT * FROM d1_role_permission WHERE id = '22');
INSERT INTO d1_role_permission (id, role_id, menu_tree_id) SELECT '23','admin','5-1'  WHERE NOT EXISTS ( SELECT * FROM d1_role_permission WHERE id = '23');
INSERT INTO d1_role_permission (id, role_id, menu_tree_id) SELECT '24','admin','5-2'  WHERE NOT EXISTS ( SELECT * FROM d1_role_permission WHERE id = '24');
INSERT INTO d1_role_permission (id, role_id, menu_tree_id) SELECT '25','admin','5-3'  WHERE NOT EXISTS ( SELECT * FROM d1_role_permission WHERE id = '25');
INSERT INTO d1_role_permission (id, role_id, menu_tree_id) SELECT '26','admin','5-4'  WHERE NOT EXISTS ( SELECT * FROM d1_role_permission WHERE id = '26');
INSERT INTO d1_role_permission (id, role_id, menu_tree_id) SELECT '27','admin','5-5'  WHERE NOT EXISTS ( SELECT * FROM d1_role_permission WHERE id = '27');
INSERT INTO d1_role_permission (id, role_id, menu_tree_id) SELECT '28','admin','6'  WHERE NOT EXISTS ( SELECT * FROM d1_role_permission WHERE id = '28');
INSERT INTO d1_role_permission (id, role_id, menu_tree_id) SELECT '29','admin','6-1'  WHERE NOT EXISTS ( SELECT * FROM d1_role_permission WHERE id = '29');
INSERT INTO d1_role_permission (id, role_id, menu_tree_id) SELECT '30','admin','6-2'  WHERE NOT EXISTS ( SELECT * FROM d1_role_permission WHERE id = '30');
INSERT INTO d1_role_permission (id, role_id, menu_tree_id) SELECT '31','admin','6-3'  WHERE NOT EXISTS ( SELECT * FROM d1_role_permission WHERE id = '31');
INSERT INTO d1_role_permission (id, role_id, menu_tree_id) SELECT '32','admin','7'  WHERE NOT EXISTS ( SELECT * FROM d1_role_permission WHERE id = '32');
INSERT INTO d1_role_permission (id, role_id, menu_tree_id) SELECT '33','admin','7-1'  WHERE NOT EXISTS ( SELECT * FROM d1_role_permission WHERE id = '33');
INSERT INTO d1_role_permission (id, role_id, menu_tree_id) SELECT '34','admin','7-2'  WHERE NOT EXISTS ( SELECT * FROM d1_role_permission WHERE id = '34');
INSERT INTO d1_role_permission (id, role_id, menu_tree_id) SELECT '35','admin','7-3'  WHERE NOT EXISTS ( SELECT * FROM d1_role_permission WHERE id = '35');
-- INSERT INTO d1_role_permission (id, role_id, menu_tree_id) SELECT '36','admin','7-4'  WHERE NOT EXISTS ( SELECT * FROM d1_role_permission WHERE id = '36');
