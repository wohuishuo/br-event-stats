package com.bookrealm.stats;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 书域 MVP-3 · 事件统计服务。
 * 职责:消费 UserLogin 事件(RabbitMQ fanout)、接收阅读进度上报(HTTP)、提供聚合查询。
 */
@SpringBootApplication
public class StatsApplication {
    public static void main(String[] args) {
        SpringApplication.run(StatsApplication.class, args);
    }
}
