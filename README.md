# 📊 事件统计服务 · br-event-stats

**[书域 BookRealm](https://github.com/wohuishuo/book-realm) 电子书平台的统计模块(MVP-3)**

书域是拆成 5 个独立模块的电子书平台;本仓负责**行为统计**:
用户中心发布登录事件,本服务通过 RabbitMQ 消费并聚合;阅读 App 通过 HTTP 上报阅读进度。

> ✅ MVP-3 第一版已完成:UserLogin 事件消费、登录日志、登录聚合、阅读进度上报、统计查询、4 条测试、真实联调均通过。

## 它解决什么

登录和阅读是主链路,统计是旁路。旁路不能拖慢主链路。

```
用户中心登录成功
   │ 发布 UserLogin
   ▼
RabbitMQ fanout: user.events
   ├─ login.log   → LoginLogConsumer   → login_logs
   └─ login.stats → LoginStatsConsumer → login_stats

阅读 App
   └─ POST /api/stats/progress → reading_stats
```

App 不直连 RabbitMQ;移动端只调用 HTTP API。

## 快速开始

```powershell
# 1. 启动依赖
rabbitmq-server -detached
docker exec bookrealm-library-mysql mysql -uroot -e "CREATE DATABASE IF NOT EXISTS book_realm_stats DEFAULT CHARACTER SET utf8mb4;"

# 2. 启动服务
mvn spring-boot:run

# 3. 健康检查
curl http://localhost:8083/api/health
```

Swagger: <http://localhost:8083/api/swagger-ui.html>

## API

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET | `/api/health` | 健康检查 |
| GET | `/api/stats/logins?from=&to=` | 查询登录统计 |
| POST | `/api/stats/progress` | App 上报阅读进度 |
| GET | `/api/stats/reading?from=&to=` | 查询阅读统计 |

阅读进度请求:

```json
{
  "userId": 2,
  "bookId": 1,
  "chapterId": 1,
  "paragraphIndex": 7
}
```

## 真实验证

```powershell
# 阅读进度
curl -X POST http://localhost:8083/api/stats/progress `
  -H "Content-Type: application/json" `
  -d "{\"userId\":2,\"bookId\":1,\"chapterId\":1,\"paragraphIndex\":7}"

# 触发用户中心真实登录事件
curl -X POST http://localhost/api/user/login `
  -H "Content-Type: application/json" `
  -d "{\"userAccount\":\"root\",\"userPassword\":\"12345678\",\"loginType\":\"App\"}"

# 查询登录统计
curl http://localhost:8083/api/stats/logins
```

实测结果:登录后当天 `appLogins=1,total=1`;阅读进度接口写入 `reading_stats`。

## 项目文档

| 文档 | 内容 |
| --- | --- |
| [`docs/design.md`](docs/design.md) | 事件驱动设计、表结构、接口边界 |
| [`docs/notes.md`](docs/notes.md) | 真实踩坑与联调记录 |
| [平台书 · MVP-3 实战章](https://wohuishuo.github.io/book-realm/project/event-stats) | 平台视角讲解 |

## 测试

```powershell
mvn test
```

当前 4 条测试:

- UserLogin 事件写登录日志并聚合 App 登录数;
- 阅读进度按 user+book+day upsert;
- 阅读进度 HTTP 接口可用;
- 登录/阅读查询接口返回数组。
