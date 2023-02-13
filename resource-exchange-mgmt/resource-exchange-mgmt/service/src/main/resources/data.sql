-- 初始化数据库库表数据
--1. 管理用户初始化一个admin用户
INSERT INTO d1_web_admin_user (ID,NAME, PASSWORD) SELECT
	'admin',
	'admin',
	'e10adc3949ba59abbe56e057f20f883e'
WHERE
	NOT EXISTS (
		SELECT
			*
		FROM
			d1_web_admin_user
		WHERE
			ID = 'admin'
	);

-- 2. 管理中心服务配置初始化
INSERT INTO d1_management_config (ID,CONFIG_KEY,CONFIG_VALUE,CONFIG_NAME,CREATE_TIME) SELECT '1', 'manage.center.url', '', '管理中心服务根地址', DATE(NOW()) WHERE NOT EXISTS ( SELECT * FROM d1_management_config WHERE CONFIG_KEY = 'manage.center.url' );
INSERT INTO d1_management_config (ID,CONFIG_KEY,CONFIG_VALUE,CONFIG_NAME,CREATE_TIME) SELECT '2', 'manage.center.mq.url', '', '消息队列服务地址', DATE(NOW()) WHERE NOT EXISTS ( SELECT * FROM d1_management_config WHERE CONFIG_KEY = 'manage.center.mq.url' );
INSERT INTO d1_management_config (ID,CONFIG_KEY,CONFIG_VALUE,CONFIG_NAME,CREATE_TIME) SELECT '3', 'build.service.appid', '', '打包服务appid', DATE(NOW()) WHERE NOT EXISTS ( SELECT * FROM d1_management_config WHERE CONFIG_KEY = 'build.service.appid' );
INSERT INTO d1_management_config (ID,CONFIG_KEY,CONFIG_VALUE,CONFIG_NAME,CREATE_TIME) SELECT '4', 'build.service.appkey', '', '打包服务appkey', DATE(NOW()) WHERE NOT EXISTS ( SELECT * FROM d1_management_config WHERE CONFIG_KEY = 'build.service.appkey' );
INSERT INTO d1_management_config (ID,CONFIG_KEY,CONFIG_VALUE,CONFIG_NAME,CREATE_TIME) SELECT '5', 'file.manage.appid', '', '文件管理appid', DATE(NOW()) WHERE NOT EXISTS ( SELECT * FROM d1_management_config WHERE CONFIG_KEY = 'file.manage.appid' );
INSERT INTO d1_management_config (ID,CONFIG_KEY,CONFIG_VALUE,CONFIG_NAME,CREATE_TIME) SELECT '6', 'file.manage.appkey', '', '文件管理appkey', DATE(NOW()) WHERE NOT EXISTS ( SELECT * FROM d1_management_config WHERE CONFIG_KEY = 'file.manage.appkey' );
INSERT INTO d1_management_config (ID,CONFIG_KEY,CONFIG_VALUE,CONFIG_NAME,CREATE_TIME) SELECT '7', 'official.nexus.url', '', '官方NEXUS库地址', DATE(NOW()) WHERE NOT EXISTS ( SELECT * FROM d1_management_config WHERE CONFIG_KEY = 'official.nexus.url' );
INSERT INTO d1_management_config (ID,CONFIG_KEY,CONFIG_VALUE,CONFIG_NAME,CREATE_TIME) SELECT '8', 'manage.center.mq.ws.url', '', '消息队列服务地址（ws）', DATE(NOW()) WHERE NOT EXISTS ( SELECT * FROM d1_management_config WHERE CONFIG_KEY = 'manage.center.mq.ws.url' );

-- 3. 新建mqtt_user表
CREATE TABLE IF NOT EXISTS mqtt_user (
	id SERIAL primary key,
	is_superuser boolean,
	username character varying(100),
	password character varying(100),
	salt character varying(40));
-- 4. 新增一个mqtt用户，作为管理中心使用
INSERT INTO mqtt_user (ID,IS_SUPERUSER,USERNAME,PASSWORD,SALT) SELECT '1', 't', 'admin', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'salt' WHERE NOT EXISTS ( SELECT * FROM mqtt_user WHERE id = '1' );
