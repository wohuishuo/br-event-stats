package com.bookrealm.stats.event;

import com.bookrealm.stats.config.RabbitMQConfig;
import com.bookrealm.stats.service.StatsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class LoginLogConsumer {
    private final StatsService statsService;

    public LoginLogConsumer(StatsService statsService) {
        this.statsService = statsService;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_LOGIN_LOG)
    public void handle(UserLoginEvent event) {
        statsService.recordLoginLog(event);
    }
}
