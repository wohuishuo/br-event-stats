package com.bookrealm.stats.service;

import com.bookrealm.stats.dto.LoginStatsResponse;
import com.bookrealm.stats.dto.ReadingProgressRequest;
import com.bookrealm.stats.dto.ReadingStatsResponse;
import com.bookrealm.stats.entity.LoginLog;
import com.bookrealm.stats.entity.LoginStats;
import com.bookrealm.stats.entity.ReadingStats;
import com.bookrealm.stats.event.UserLoginEvent;
import com.bookrealm.stats.repository.LoginLogRepository;
import com.bookrealm.stats.repository.LoginStatsRepository;
import com.bookrealm.stats.repository.ReadingStatsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class StatsService {
    private final LoginLogRepository loginLogRepository;
    private final LoginStatsRepository loginStatsRepository;
    private final ReadingStatsRepository readingStatsRepository;

    public StatsService(LoginLogRepository loginLogRepository,
                        LoginStatsRepository loginStatsRepository,
                        ReadingStatsRepository readingStatsRepository) {
        this.loginLogRepository = loginLogRepository;
        this.loginStatsRepository = loginStatsRepository;
        this.readingStatsRepository = readingStatsRepository;
    }

    @Transactional
    public void recordLoginLog(UserLoginEvent event) {
        validateLoginEvent(event);
        loginLogRepository.save(new LoginLog(
                event.userId(),
                normalizeLoginType(event.loginType()),
                event.loginTime() == null ? LocalDateTime.now() : event.loginTime(),
                event.ipAddress()
        ));
    }

    @Transactional
    public void increaseLoginStats(UserLoginEvent event) {
        validateLoginEvent(event);
        LocalDate date = (event.loginTime() == null ? LocalDateTime.now() : event.loginTime()).toLocalDate();
        LoginStats stats = loginStatsRepository.findByStatsDate(date).orElseGet(() -> new LoginStats(date));
        stats.increase(normalizeLoginType(event.loginType()));
        loginStatsRepository.save(stats);
    }

    @Transactional
    public ReadingStatsResponse reportReadingProgress(ReadingProgressRequest req) {
        LocalDate today = LocalDate.now();
        ReadingStats stats = readingStatsRepository
                .findByUserIdAndBookIdAndStatsDate(req.getUserId(), req.getBookId(), today)
                .orElseGet(() -> new ReadingStats(
                        req.getUserId(),
                        req.getBookId(),
                        req.getChapterId(),
                        req.getParagraphIndex(),
                        today
                ));
        if (stats.getId() != null) {
            stats.updateProgress(req.getChapterId(), req.getParagraphIndex());
        }
        return ReadingStatsResponse.from(readingStatsRepository.save(stats));
    }

    @Transactional(readOnly = true)
    public List<LoginStatsResponse> loginStats(LocalDate from, LocalDate to) {
        return loginStatsRepository.findByStatsDateBetweenOrderByStatsDate(from, to)
                .stream()
                .map(LoginStatsResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ReadingStatsResponse> readingStats(LocalDate from, LocalDate to) {
        return readingStatsRepository.findByStatsDateBetweenOrderByStatsDateAscUserIdAsc(from, to)
                .stream()
                .map(ReadingStatsResponse::from)
                .toList();
    }

    private void validateLoginEvent(UserLoginEvent event) {
        if (event == null || event.userId() == null) {
            throw new IllegalArgumentException("UserLogin event missing userId");
        }
    }

    private String normalizeLoginType(String loginType) {
        if (loginType == null || loginType.isBlank()) {
            return "Web";
        }
        String type = loginType.trim();
        if ("app".equalsIgnoreCase(type)) {
            return "App";
        }
        if ("desktop".equalsIgnoreCase(type)) {
            return "Desktop";
        }
        return "Web";
    }
}
