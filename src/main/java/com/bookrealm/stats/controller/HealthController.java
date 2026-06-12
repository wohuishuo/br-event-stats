package com.bookrealm.stats.controller;

import com.bookrealm.stats.common.BaseResponse;
import com.bookrealm.stats.common.ResultUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "系统")
@RestController
public class HealthController {

    @Operation(summary = "健康检查")
    @GetMapping("/health")
    public BaseResponse<String> health() {
        return ResultUtils.success("br-event-stats is up");
    }
}
