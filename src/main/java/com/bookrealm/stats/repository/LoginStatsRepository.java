package com.bookrealm.stats.repository;

import com.bookrealm.stats.entity.LoginStats;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LoginStatsRepository extends JpaRepository<LoginStats, Long> {
    Optional<LoginStats> findByStatsDate(LocalDate statsDate);
    List<LoginStats> findByStatsDateBetweenOrderByStatsDate(LocalDate from, LocalDate to);
}
