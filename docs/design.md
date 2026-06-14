# 设计说明

> **结论先行**:MVP-3 只统计两件事——登录和阅读进度。登录走 RabbitMQ 异步事件,阅读进度走 HTTP 上报;这样既能证明事件驱动,又不把移动端拖进消息队列。

## 一、边界

统计服务不负责登录,也不负责阅读内容。它只回答:

- 谁在什么时候登录了?
- 今天 App/Web/Desktop 各有多少登录?
- 用户读到了哪本书、哪一章、哪一段?

## 二、事件链路

```
MVP-0 用户中心
  └─ 登录成功后发布 UserLogin
       ▼
RabbitMQ fanout exchange: user.events
       ├─ login.log   → 原始日志
       └─ login.stats → 聚合统计
```

fanout 的价值是:一个事件可以同时被多个消费者使用。保存原始日志和做聚合统计互不影响。

## 三、阅读进度链路

```
MVP-2 阅读 App
  └─ POST /api/stats/progress
       ▼
MVP-3 reading_stats
```

App 不直连 MQ。手机端走 HTTP 更安全、更容易调试,也符合服务边界。

## 四、表设计

| 表 | 作用 |
| --- | --- |
| `login_logs` | 每次登录原始记录 |
| `login_stats` | 按日期聚合 App/Web/Desktop 登录数 |
| `reading_stats` | 按 user+book+day 保存阅读进度 |

`reading_stats` 用 `user_id + book_id + stats_date` 做唯一键。同一天同一本书多次上报,更新同一行,`reportCount +1`。

## 五、接口设计

| 接口 | 说明 |
| --- | --- |
| `GET /api/stats/logins?from=&to=` | 查询登录聚合 |
| `POST /api/stats/progress` | 阅读 App 上报进度 |
| `GET /api/stats/reading?from=&to=` | 查询阅读统计 |

统一返回 `{code,data,message}`,与用户中心、书库保持一致。

## 六、验收

- `mvn test` 4 条测试全绿;
- RabbitMQ 管理台可见 `user.events`、`login.log`、`login.stats`;
- 用户中心真实登录后,`/api/stats/logins` 出现 `appLogins +1`;
- 调 `/api/stats/progress` 后,`/api/stats/reading` 能查到进度。
