# Changelog

这个项目的所有显著变化都将被记录在这个文件中。

# [4.0.0-RELEASE] -2019-06-05

- 替换新的角色权限体系
- 新增Permission注解 支持自动扫描接口权限
- 移除ExtensionAttribute注解 替换为EnableExtensionAttribute注解
- BaseDTO createdBy和creationDate字段禁止更新
- DataSet不传分页信息，由原先默认查10条数据改为默认查询全部数据
- LOV 查询 map传参转换为DTO 优先使用mapper传参类型
- 修复RabbitMQ message为空 NPE问题
- 修复组织管理 更新是否启用字段无效问题
- 修复登录页面logo和favicon不能修改
- 修复mybatis默认传参名问题 手动通过@Param("xx")指定参数名
- 修复LOV 日期时间作为条件查询时报错
- 修复计划任务 设置开始、结束时间导致NPE问题
- 修复接口透传 oauth2认证问题
- 修复审计和版本号拦截器顺序问题 导致审计查询结果为空



