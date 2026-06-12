package com.bookrealm.stats.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ 拓扑(架构已定,勿改名):
 * fanout exchange "user.events" → 两个队列 login.log / login.stats。
 * 用户中心发布 UserLogin 事件到 exchange,广播进两个队列,由本服务两个消费者分别处理。
 */
@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_USER_EVENTS = "user.events";
    public static final String QUEUE_LOGIN_LOG = "login.log";
    public static final String QUEUE_LOGIN_STATS = "login.stats";

    @Bean
    public FanoutExchange userEventsExchange() {
        return new FanoutExchange(EXCHANGE_USER_EVENTS, true, false);
    }

    @Bean
    public Queue loginLogQueue() {
        return new Queue(QUEUE_LOGIN_LOG, true);
    }

    @Bean
    public Queue loginStatsQueue() {
        return new Queue(QUEUE_LOGIN_STATS, true);
    }

    @Bean
    public Binding bindLog(FanoutExchange userEventsExchange, Queue loginLogQueue) {
        return BindingBuilder.bind(loginLogQueue).to(userEventsExchange);
    }

    @Bean
    public Binding bindStats(FanoutExchange userEventsExchange, Queue loginStatsQueue) {
        return BindingBuilder.bind(loginStatsQueue).to(userEventsExchange);
    }
}
