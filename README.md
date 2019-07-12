# HAP

[![build status](https://rdc.hand-china.com/gitlab/HAP/hap/badges/master/build.svg)](https://rdc.hand-china.com/gitlab/HAP/hap/commits/master)

[3.x 开发文档](http://eco.hand-china.com/doc/hap/latest/)

[4.x 开发文档](https://wiki.choerodon.com.cn/bin/view/O-Choerodon/P-HAP%E6%A1%86%E6%9E%B6%E7%BB%84/HAP4.0%E5%BC%80%E5%8F%91%E6%89%8B%E5%86%8C/)


## 项目模块

- hap-front-kendo kendoUI 相关部分
- hap-front-html 原kendoUI 实现的功能界面
- hap 框架汇总模块 (包含其他分模块 提供 HAP 完整功能)
- hap-core 框架核心模块 (包含组织架构、系统管理等基础功能)
- hap-gateway 服务管理 (在接口管理基础上 重新优化设计 推荐使用服务管理)
- hap-interface 接口管理 (原是从其他系统移植过来的功能)
- hap-job 计划任务 (基于Quartz管理 自动执行)
- hap-mail 邮件服务 (基于JavaMail)
- hap-oauth2 Oauth认证服务 (基于OAuth2)
- hap-report 报表服务 (基于UReport2)
- hap-task 任务管理 (基于HAP权限控制 手动执行)
- hap-workflow 工作流 (基于Activiti)
- hap-attachment 附件管理
- hap-cxf-ws 发布WebService (基于CXF)
- hap-security-standard 标准登录 (基于spring-boot-starter-security)
- hap-security-cas CAS单点登录 (基于spring-security-cas)
- hap-security-ldap LDAP认证 (基于spring-security-ldap)
- hap-modeling 建模平台(已弃用)

## 创建数据库

默认数据库为MySQL，修改MySQL的配置文件 my.cnf (or my.ini),在 [mysqld] 下添加
lower_case_table_names=1
character_set_server=utf8
max_connections=500

执行以下SQL，创建默认数据库
CREATE SCHEMA hap_dev DEFAULT CHARACTER SET utf8;
CREATE USER hap_dev@'%' IDENTIFIED BY 'hap_dev';
GRANT ALL PRIVILEGES ON hap_dev.* TO hap_dev@'%';
FLUSH PRIVILEGES;


## 初始化数据库

根目录下执行如下命令（ Linux/Mac 可以直接执行，Windows可安装Git后使用Git Bash执行）
```sh
    sh init-database.sh
```

### 脚本参数介绍
- Dspring.datasource.url： 数据源url
- Dspring.datasource.username：数据库登陆用户
- Ddata.drop=false：是否清除数据库数据
- Ddata.init：是否使用excel数据进行数据初始化
- Ddata.dir：groovy和excel所在的文件夹，扫描指定文件夹下的所有.groovy和.xlsx文件
- Ddata.mode : 数据初始化的模式可选：normal（默认，初始化groovy和excel），iam（初始化权限数据，仅单体模式需要），all（二者并集）
- Ddata.update.exclusion：初始化数据库更新数据的时候忽略的表或列，忽略表直接写表名，表名.列名 表示忽略某一列，使用逗号分割

## 启动前准备
   修改配置文件application.yml 文件配置数据库与相关连接信息。示例如下：
```yml
spring:
  datasource:
    password: ${MYSQL_PASS:hap_dev}
    url: jdbc:mysql://${MYSQL_HOST:localhost}:${MYSQL_PORT:3306}/${MYSQL_DB:hap_dev}?useSSL=false&allowPublicKeyRetrieval=true
    username: ${MYSQL_USER:hap_dev}
redis:
  ip: ${REDIS_IP:localhost}
  db: ${REDIS_DB:10}
db:
  type: mysql
mybatis:
  identity: JDBC
```

  如果没有依赖安全模块（hap-security-standard、hap-security-cas、hap-security-ldap）
   
  IRequest中默认配置了admin用户的信息，可在application.yml中自定义配置，示例如下

```yml
default:
  security:
    userId: 10002
    userName: jessen
    roleId: 10001
    allRoleId: 10002,10001
    companyId: 10001
    employeeCode: JESSEN
    locale: en_GB
```

  权限校验时，如果permission未注册，默认会拦截并返回无权限

  可在application.yml中配置是否跳过权限校验，示例如下(线上环境无需配置 默认强制进行权限校验,本地开发可设置为true 方便调试)

```yml
default:
  permission:
    access: true 
```
## 本地开发

如果是本地开发，建议单独启动前后端

### 单独启动后端项目
hap目录启动所有模块，各个模块下单独启动对应服务，命令如下：
```mvn
    mvn spring-boot:run
```
可以使用postman等工具 访问localhost:8080进行测试

### 单独启动前端项目
hap目录下执行,命令如下：
```text
    mvn clean package
    npm install --registry https://nexus.choerodon.com.cn/repository/choerodon-npm/
    npm start
```
注：如果是linux系统，在npm start失败时可以先尝试给node_modules增加权限
```
chmod -R 755 node_modules
```
目前如果只启动前端是无法登陆访问界面的，
需要在config.js里面配置proxyTarget（本地或者远程后端服务器地址），
如proxyTarget: 'http://localhost:8080'

然后浏览器访问 localhost:9090 访问HAP界面

## 部署项目

如果是要测试下项目整体运行或者部署项目，建议前后端一起运行

### 前后端一起运行
- 首先在 hap 目录运行 npm run build 打包前端
- 在 hap 目录执行以下  mvn 命令：
```mvn
    mvn spring-boot:run
```
浏览器访问 localhost:8080 访问HAP界面
