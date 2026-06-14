package com.bookrealm.stats.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "login_stats", uniqueConstraints = @UniqueConstraint(name = "uk_login_stats_date", columnNames = "stats_date"))
public class LoginStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "stats_date", nullable = false)
    private LocalDate statsDate;

    @Column(nullable = false)
    private long appLogins = 0;

    @Column(nullable = false)
    private long webLogins = 0;

    @Column(nullable = false)
    private long desktopLogins = 0;

    @Column(nullable = false)
    private long total = 0;

    @Column(nullable = false)
    private LocalDateTime updateTime = LocalDateTime.now();

    protected LoginStats() {}

    public LoginStats(LocalDate statsDate) {
        this.statsDate = statsDate;
    }

    public void increase(String loginType) {
        String type = loginType == null ? "" : loginType.trim().toLowerCase();
        if ("app".equals(type)) {
            appLogins++;
        } else if ("desktop".equals(type)) {
            desktopLogins++;
        } else {
            webLogins++;
        }
        total++;
        updateTime = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public LocalDate getStatsDate() { return statsDate; }
    public long getAppLogins() { return appLogins; }
    public long getWebLogins() { return webLogins; }
    public long getDesktopLogins() { return desktopLogins; }
    public long getTotal() { return total; }
    public LocalDateTime getUpdateTime() { return updateTime; }
}
