# 📊 事件统计服务 · br-event-stats

**[书域 BookRealm](https://github.com/wohuishuo/book-realm) 电子书平台的统计模块(MVP-3)**

书域是拆成 5 个独立模块的电子书平台;本仓负责**行为统计**:
用户中心发布的登录事件经 RabbitMQ 广播到两个队列,本服务的日志/统计消费者分别落库;App 的阅读进度走 HTTP 上报。

> 🚧 骨架阶段:工程配置与 MQ 拓扑(fanout `user.events` → `login.log` / `login.stats`)已就绪并验证可启动;消费者与查询接口按 [工单](https://github.com/wohuishuo/book-realm/blob/main/工单-MVP3事件统计.md) 开发中。

## 快速开始

```powershell
# 后端依赖一键起(MySQL/MQ 等):book-realm 仓的 ./start-platform.ps1
mvn spring-boot:run     # :8083,Swagger: http://localhost:8083/api/swagger-ui.html
```

讲解见平台书:[MVP-3 实战章](https://wohuishuo.github.io/book-realm/project/event-stats)
