package com.bookrealm.stats;

import com.bookrealm.stats.dto.ReadingProgressRequest;
import com.bookrealm.stats.event.UserLoginEvent;
import com.bookrealm.stats.repository.LoginLogRepository;
import com.bookrealm.stats.repository.LoginStatsRepository;
import com.bookrealm.stats.repository.ReadingStatsRepository;
import com.bookrealm.stats.service.StatsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:stats-test;MODE=MySQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.rabbitmq.listener.simple.auto-startup=false"
})
class StatsServiceTest {
    @Autowired
    StatsService statsService;
    @Autowired
    LoginLogRepository loginLogRepository;
    @Autowired
    LoginStatsRepository loginStatsRepository;
    @Autowired
    ReadingStatsRepository readingStatsRepository;

    @Test
    void userLoginEventShouldCreateLogAndAggregateStats() {
        UserLoginEvent event = new UserLoginEvent(2L, "App", LocalDateTime.now(), "127.0.0.1");

        statsService.recordLoginLog(event);
        statsService.increaseLoginStats(event);

        assertThat(loginLogRepository.count()).isEqualTo(1);
        var today = loginStatsRepository.findByStatsDate(LocalDate.now()).orElseThrow();
        assertThat(today.getAppLogins()).isEqualTo(1);
        assertThat(today.getTotal()).isEqualTo(1);
    }

    @Test
    void reportReadingProgressShouldUpsertDailyUserBookStats() {
        ReadingProgressRequest first = request(2L, 1L, 1L, 3);
        ReadingProgressRequest second = request(2L, 1L, 1L, 8);

        statsService.reportReadingProgress(first);
        var response = statsService.reportReadingProgress(second);

        assertThat(readingStatsRepository.count()).isEqualTo(1);
        assertThat(response.reportCount()).isEqualTo(2);
        assertThat(response.paragraphIndex()).isEqualTo(8);
    }

    private ReadingProgressRequest request(Long userId, Long bookId, Long chapterId, int paragraphIndex) {
        ReadingProgressRequest request = new ReadingProgressRequest();
        request.setUserId(userId);
        request.setBookId(bookId);
        request.setChapterId(chapterId);
        request.setParagraphIndex(paragraphIndex);
        return request;
    }
}
