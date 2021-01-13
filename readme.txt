1 服务网关的要素
2 常用的网关方案
3 Zuul及特点
4 创建项目及测试
	4.1 创建zuul-proxy模块
	4.2 默认路由规则
	4.3 配置路由规则
	4.4 配置访问前缀
	4.5 Header过滤及重定向添加Host
	4.6 查看路由信息
	4.7 过滤器
5 Zuul的高可用


1 服务网关的要素
a) 稳定性、高可用
b) 性能、并发性
c) 安全性
d) 扩展性

2 常用的网关方案
a) Nginx+Lua
性能和稳定性不错
b) Kong
要花钱的
c) Tyk
轻量级，go语言
d) Spring Cloud Zuul

3 Zuul及特点
a) 路由
b) 过滤器
c) 负载均衡
4 创建项目及测试
4.1 创建zuul-proxy模块
引入依赖
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-zuul</artifactId>
</dependency>

启动类上添加注解
@EnableZuulProxy

4.2 默认路由规则
Zuul和Eureka结合使用，可以实现路由的自动配置，自动配置的路由以服务名称为匹配路径，相当于如下配置：
zuul:
  routes:
    # 给服务配置路由
    user-service:
      path: /user-service/**

负载均衡功能
访问地址 http://localhost:9768/user-service/test/test1，观察结果，可以看出在user1和user2服务之间切换

4.3 配置路由规则
两种方式
方式1
zuul:
  routes:
    # 第一种写法 访问 /bid/test/test1 映射到 /user-service/test/test1
    # 唯一标记
    aaa:
      path: /bid/**
      serviceId: user-service

方式2
zuul:
  routes:
    # 第二种简洁写法 访问 /bid/test/test1 映射到 /user-service/test/test1
    # 只有一条路由规则时可以写成 ${服务ID}: ${访问路径规则} 形式，如下：
#    user-service: /bid/**

    # 有多条路由规则时
    user-service:
      path: /bid/**

测试
http://localhost:9768/bid/test/test1

4.4 配置访问前缀
zuul:
  # 给网关路由添加前缀
  prefix: /auction

测试
http://localhost:9768/auction/bid/test/test1

4.5 Header过滤及重定向添加Host
zuul:
  # 设置为true重定向是会添加host请求头
  add-host-header: true

  # 全局敏感头，配置要过滤的敏感请求头信息。为空表示不过滤请求headers，这样在路由到其它服务时，cookies等信息也会传递过去
  sensitive-headers:

4.6 查看路由信息
在pom.xml中添加相关依赖
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>

修改bootstrap.yml配置文件，开启查看路由的端点
management:
  endpoints:
    web:
      exposure:
        include:  "*"

// 查看简单路由信息
http://localhost:9768/actuator/routes
{
"/auction/bid/**": "user-service",
"/auction/user-service/**": "user-service"
}

// 查看详细路由信息
http://localhost:9768/actuator/routes/details
{
	"/auction/bid/**": {
		"id": "user-service",
		"fullPath": "/auction/bid/**",
		"location": "user-service",
		"path": "/**",
		"prefix": "/auction/bid",
		"retryable": false,
		"customSensitiveHeaders": false,
		"prefixStripped": true
	},
	"/auction/user-service/**": {
		"id": "user-service",
		"fullPath": "/auction/user-service/**",
		"location": "user-service",
		"path": "/**",
		"prefix": "/auction/user-service",
		"retryable": false,
		"customSensitiveHeaders": false,
		"prefixStripped": true
	}
}

4.7 过滤器
a) 过滤器类型
    pre filters
    routing filter(s)
    post filter(s) //已经拿到结果，可以对结果的处理或加工
    error filter //上面三个发生异常时走过个
    custom filter //自定义filter，可以放在prefilter和postfilter
f) 前置Pre可以做：
	* 限流
	* 鉴权
	* 参数检验
g) 后置Post可以做：
	* 统计
	* 日志

h) 配置禁用filter
zuul:
  # 配置禁用的过滤器
  PreLogFilter:
    pre:
      disable: true

5 Zuul的高可用
    * 多个Zuul节点注册到Eureka Server
    * Nginx和Zuul混搭
为zuul-proxy添加lb-zuul-proxy，所有请求通过lb-zuul-proxy请求转发
待完善



Reference
https://blog.csdn.net/ThinkWon/article/details/103738851
