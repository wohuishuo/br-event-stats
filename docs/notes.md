# 实现笔记

> **结论先行**:MVP-3 最重要的坑不是代码复杂,而是服务之间的契约要对齐。事件字段、消息格式、队列名、数据库名,任何一个错了都会表现成"统计没数据"。

## 一、事件用 JSON,不要依赖生产者类

用户中心发布的是 `com.usercenter.event.UserLoginEvent`,统计服务不能依赖这个 Java 类。否则两个仓库会被类名绑死。

解决:两边都使用 Jackson JSON 消息转换器,统计服务只定义字段相同的本地 DTO:

```java
public record UserLoginEvent(
    Long userId,
    String loginType,
    LocalDateTime loginTime,
    String ipAddress
) {}
```

真实联调证明可行:用户中心登录后,统计服务成功消费并聚合。

## 二、队列和 exchange 名称要固定

固定契约:

- exchange:`user.events`
- queue:`login.log`
- queue:`login.stats`

RabbitMQ 管理台能看到它们,才说明拓扑声明成功。

## 三、阅读进度不走 MQ

App 不直连 MQ,这是架构裁决。阅读进度走:

```http
POST /api/stats/progress
```

这让手机端只需要 HTTP,不用知道 RabbitMQ 的连接、账号、队列、重试。

## 四、测试用 H2,真实联调用 MySQL + RabbitMQ

单元/接口测试使用 H2,这样 `mvn test` 不依赖本机 MySQL 和 RabbitMQ,能快速反馈业务逻辑。

真实验收再启动 MySQL/RabbitMQ,验证端到端事件流。两者分工不同,不能互相替代。

## 五、本次真实验证记录

- `mvn test`:4 tests, 0 failures;
- `GET /api/health`:返回 `br-event-stats is up`;
- `rabbitmqctl list_exchanges`:可见 `user.events fanout`;
- `rabbitmqctl list_queues`:可见 `login.log`、`login.stats`;
- `POST /api/stats/progress`:写入阅读进度;
- 用户中心 `POST /api/user/login`:触发 UserLogin;
- `GET /api/stats/logins`:返回 `appLogins=1,total=1`。
