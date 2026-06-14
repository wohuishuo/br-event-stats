package com.bookrealm.stats.controller;

import com.bookrealm.stats.common.BaseResponse;
import com.bookrealm.stats.common.ResultUtils;
import com.bookrealm.stats.dto.LoginStatsResponse;
import com.bookrealm.stats.dto.ReadingProgressRequest;
import com.bookrealm.stats.dto.ReadingStatsResponse;
import com.bookrealm.stats.service.StatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "统计")
@RestController
@RequestMapping("/stats")
public class StatsController {
    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @Operation(summary = "查询登录统计")
    @GetMapping("/logins")
    public BaseResponse<List<LoginStatsResponse>> loginStats(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        LocalDate end = to == null ? LocalDate.now() : to;
        LocalDate start = from == null ? end.minusDays(6) : from;
        return ResultUtils.success(statsService.loginStats(start, end));
    }

    @Operation(summary = "阅读进度上报")
    @PostMapping("/progress")
    public BaseResponse<ReadingStatsResponse> reportProgress(@Valid @RequestBody ReadingProgressRequest request) {
        return ResultUtils.success(statsService.reportReadingProgress(request));
    }

    @Operation(summary = "查询阅读统计")
    @GetMapping("/reading")
    public BaseResponse<List<ReadingStatsResponse>> readingStats(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        LocalDate end = to == null ? LocalDate.now() : to;
        LocalDate start = from == null ? end.minusDays(6) : from;
        return ResultUtils.success(statsService.readingStats(start, end));
    }
}
