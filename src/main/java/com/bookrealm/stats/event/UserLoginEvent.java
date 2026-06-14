package com.bookrealm.stats.event;

import java.time.LocalDateTime;

/**
 * 与用户中心发布的 UserLogin 事件字段对齐。只依赖字段名,不依赖用户中心的 Java 类。
 */
public record UserLoginEvent(
        Long userId,
        String loginType,
        LocalDateTime loginTime,
        String ipAddress
) {
}
