package com.bookrealm.stats.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "login_logs")
public class LoginLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, length = 32)
    private String loginType;

    @Column(nullable = false)
    private LocalDateTime loginTime;

    @Column(length = 64)
    private String ipAddress;

    @Column(nullable = false)
    private LocalDateTime createTime = LocalDateTime.now();

    protected LoginLog() {}

    public LoginLog(Long userId, String loginType, LocalDateTime loginTime, String ipAddress) {
        this.userId = userId;
        this.loginType = loginType;
        this.loginTime = loginTime;
        this.ipAddress = ipAddress;
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getLoginType() { return loginType; }
    public LocalDateTime getLoginTime() { return loginTime; }
    public String getIpAddress() { return ipAddress; }
    public LocalDateTime getCreateTime() { return createTime; }
}
