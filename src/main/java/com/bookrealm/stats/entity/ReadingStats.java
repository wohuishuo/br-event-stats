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
@Table(name = "reading_stats", uniqueConstraints = @UniqueConstraint(name = "uk_reading_user_book_day", columnNames = {"user_id", "book_id", "stats_date"}))
public class ReadingStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "book_id", nullable = false)
    private Long bookId;

    @Column(name = "chapter_id", nullable = false)
    private Long chapterId;

    @Column(nullable = false)
    private int paragraphIndex;

    @Column(nullable = false)
    private int reportCount = 0;

    @Column(name = "stats_date", nullable = false)
    private LocalDate statsDate;

    @Column(nullable = false)
    private LocalDateTime lastReportTime = LocalDateTime.now();

    protected ReadingStats() {}

    public ReadingStats(Long userId, Long bookId, Long chapterId, int paragraphIndex, LocalDate statsDate) {
        this.userId = userId;
        this.bookId = bookId;
        this.chapterId = chapterId;
        this.paragraphIndex = paragraphIndex;
        this.statsDate = statsDate;
        this.reportCount = 1;
    }

    public void updateProgress(Long chapterId, int paragraphIndex) {
        this.chapterId = chapterId;
        this.paragraphIndex = Math.max(this.paragraphIndex, paragraphIndex);
        this.reportCount++;
        this.lastReportTime = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public Long getBookId() { return bookId; }
    public Long getChapterId() { return chapterId; }
    public int getParagraphIndex() { return paragraphIndex; }
    public int getReportCount() { return reportCount; }
    public LocalDate getStatsDate() { return statsDate; }
    public LocalDateTime getLastReportTime() { return lastReportTime; }
}
