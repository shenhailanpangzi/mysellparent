# config-repo
springcloud项目的配置中心
访问地址示例：[/{label}/{name}-{profiles}.properties] 
name 服务名
profiles 环境名
label 分支（branch）
例如：http://localhost:8080/release/order-dev.yml

使用配置中心查看配置：http://localhost:8080/user-dev.yml

测试是否已经跟新配置接口：http://localhost:8091/env/print
更新配置接口：http://localhost:8080/actuator/bus-refresh

无论哪个环境都会加载order.yml 所以可以在order.yml中写公用配置
