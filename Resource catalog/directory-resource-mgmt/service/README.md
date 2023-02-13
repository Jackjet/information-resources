#resourcecatalog

Resourcecatalog = Spring Boot后端 + Vue管理员前端 + 微信小程序用户前端 + Vue用户移动端



### 管理后台实例

![](./doc/pics/readme/admin-dashboard.png)

 管理员用户名`admin123`，管理员密码`admin123`


## 项目架构
![](./doc/pics/readme/project-structure.png)

## 技术栈

> 1. Spring Boot
> 2. Vue
> 3. 微信小程序

![](doc/pics/readme/technology-stack.png)

## 功能

### 管理平台功能

* 系统管理
* 配置管理


## 快速启动

1. 配置最小开发环境：
    * [MySQL](https://dev.mysql.com/downloads/mysql/)
    * [JDK1.8或以上](http://www.oracle.com/technetwork/java/javase/overview/index.html)
    * [Maven](https://maven.apache.org/download.cgi)
    * [Nodejs](https://nodejs.org/en/download/)
    * [微信开发者工具](https://developers.weixin.qq.com/miniprogram/dev/devtools/download.html)
    
2. 数据库依次导入Resourcecatalog-db/sql下的数据库文件
    *resourcecatalog_schema.sql
    *resourcecatalog_table.sql
    *resourcecatalog_data.sql

3. 启动小商场和管理后台的后端服务

    打开命令行，输入以下命令
    ```bash
    cdresourcecatalog
    mvn install
    mvn clean package
    java -Dfile.encoding=UTF-8 -jarresourcecatalog-all/target/Resourcecatalog-all-0.1.0-exec.jar
    ```
    
4. 启动管理后台前端

    打开命令行，输入以下命令
    ```bash
    npm install -g cnpm --registry=https://registry.npm.taobao.org
    cdresourcecatalog/Resourcecatalog-admin
    cnpm install
    cnpm run dev
    ```
    此时，浏览器打开，输入网址`http://localhost:9527`, 此时进入管理后台登录页面。
    

##  api文档

http://host:ip/swagger-ui.html

http://host:ip/doc.html


开发环境：http://ts.scity.cn:9530
	      http://ts.scity.cn:18081
		  jdbc:postgresql://101.200.52.215:5432/resource_catalog2
测试环境：http://ts.scity.cn:9529
		  http://ts.scity.cn:18080
		  jdbc:postgresql://101.200.52.215:5432/resource_catalog