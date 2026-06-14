package com.bookrealm.stats.dto;

import com.bookrealm.stats.entity.ReadingStats;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ReadingStatsResponse(
        LocalDate date,
        Long userId,
        Long bookId,
        Long chapterId,
        int paragraphIndex,
        int reportCount,
        LocalDateTime lastReportTime
) {
    public static ReadingStatsResponse from(ReadingStats stats) {
        return new ReadingStatsResponse(
                stats.getStatsDate(),
                stats.getUserId(),
                stats.getBookId(),
                stats.getChapterId(),
                stats.getParagraphIndex(),
                stats.getReportCount(),
                stats.getLastReportTime()
        );
    }
}
