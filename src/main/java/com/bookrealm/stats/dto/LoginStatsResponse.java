package com.bookrealm.stats.dto;

import com.bookrealm.stats.entity.LoginStats;

import java.time.LocalDate;

public record LoginStatsResponse(
        LocalDate date,
        long appLogins,
        long webLogins,
        long desktopLogins,
        long total
) {
    public static LoginStatsResponse from(LoginStats stats) {
        return new LoginStatsResponse(
                stats.getStatsDate(),
                stats.getAppLogins(),
                stats.getWebLogins(),
                stats.getDesktopLogins(),
                stats.getTotal()
        );
    }
}
