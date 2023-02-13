<!--
 * @Author: lyy
 * @Date: 2020-06-20 19:28:58
 * @LastEditTime: 2020-06-20 21:12:48
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \webadmin\README.md
--> 
运行管理平台 后台管理页面


# 启动服务
npm run dev

# 构建测试环境
npm run build:stage

# 构建生产环境
npm run build:prod


# 单点登录
http://47.105.140.86:9002/auth
http://106.116.160.122:9001/auth

打包问题解决方案 

    1.  先卸载 npm uninstall image-webpack-loader  

    2. 用cnpm 重新安装 cnpm install image-webpack-loader --save-dev