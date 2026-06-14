package com.bookrealm.stats.repository;

import com.bookrealm.stats.entity.ReadingStats;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReadingStatsRepository extends JpaRepository<ReadingStats, Long> {
    Optional<ReadingStats> findByUserIdAndBookIdAndStatsDate(Long userId, Long bookId, LocalDate statsDate);
    List<ReadingStats> findByStatsDateBetweenOrderByStatsDateAscUserIdAsc(LocalDate from, LocalDate to);
}
